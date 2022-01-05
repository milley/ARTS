# Rust数据结构中的循环引用

[Rust data structures with circular references](https://eli.thegreenplace.net/2021/rust-data-structures-with-circular-references/)

为了实现它的安全保障，Rust编译器对程序保持谨慎的所有权和引用。这对于某些特定种类的数据结构是具有挑战的。特别是，数据结构中包含有循环引用的类型。

让我们看一个简单的二叉树:

```rust
struct Tree {
    root: Option<Node>,
}

struct Node {
    data: i32,
    left: Option<Box<Node>>,
    right: Option<Box<Node>>,
}
```

当Rust编译器应该可以在编译阶段计算结构体的大小，left和right通常使用了堆内存分配方式Box。这些盒子被包裹到Option类型因为left或right子节点有可能是空的。

现在我们希望将每个节点连接到父节点。这对于三种数据结构非常有用；例如，在搜索二叉树中，父子连接可以用来很快的找到节点的子节点。我们如何做这个？

## “明显”失败的方法

我们不能仅仅给Node增加 parent: Option<Box<Node>> ，因为这就意味着一个节点拥有其父节点；很明显这样是错的。事实上，原来的 Node 定义是父节点拥有子节点，而不是相反的。

所以大概想增加一个引用来替代；一个父节点拥有子节点，但是一个子节点参考了父节点。听起来是对的，我们试一试：

```rust
struct Node {
    data: i32,
    left: Option<Box<Node>>,
    right: Option<Box<Node>>,
    parent: Option<&Node>,
}
```

Rust将会拒绝编译，询问一个显式的生命周期参数。当我们把引用存储在struct的字段中，Rust希望知道这个引用的生命周期如何关系到结构体自身。非常公平，我们可以这样做：

```rust
struct Tree<'a> {
    root: Option<Node<'a>>,
}

struct Node<'a> {
    data: i32,
    left: Option<Box<Node<'a>>>,
    right: Option<Box<Node<'a>>>,
    parent: Option<&'a Node<'a>>,
}
```

现在生命周期是明确的：我们告诉编译器parent引用的生命周期和Node自身生命周期一样。这个结构体定义可以被编译，但是当写真正操纵它的代码时会很快的被借用检查拦截下来。考虑到会给当前节点插入一个新的节点；为了改变当前节点，在生效范围内必须有可变引用。在同样的时间，新的子节点的父节点连接也是一个节点引用。借用检查不允许我们创建一个引用指向已经活动的可变引用；同样不允许我们改变一个引用其已经是其他的活跃引用。

作为练习，使用这个Node定义来编写一个插入的方法；你很快就会得到相同的结论。

## 怎么办？

很明确，“明显的”道路走不下去。考虑下最早的原则，它不应该。在这边文章中我使用带父节点的二分查找树来作为例子学习，但还有更多明显的例子。考虑到图数据结构，一个节点有指向其他节点的边，两个节点也很容易的指向对方。谁拥有谁？一边情况下这个问题在编译期间无法回答，意味着我们不能使用普通的rust引用来指向它们的关系。我们需要更聪明一些。

在一次又一次的提到这个问题，rust程序员留在了3个潜在的方案：

1. 推迟借用检查到运行时，但是使用引用计数指针(std::rc::Rc)指向std::cell::Cell。
2. 集中所有权(例如所有的节点被Tree中的一组节点拥有)，然后引用变为句柄(索引到上述的向量中)。
3. 使用原始指针和unsafe块。

在这篇文章中，我将展示每种方法，应用于实现一个合理的功能完备的搜索二叉树，包含插入、删除、获取继任者方法和中序遍历。完整的代码在[这个仓库](https://github.com/eliben/code-for-blog/tree/master/2021/rust-bst)；文章仅仅展示了关键的代码片段。参考代码仓库中完整的实现和全面的测试。

## 使用 Rc 和 RefCell 运行期检查

这个方法使用了2个Rust标准库中的数据结构：

- std::rc::Rc 是引用计数指针，提供了堆内存分配的共享所有权。Rc的多个实例可以指向同一个数据；当所有的引用都超出作用域后，堆内存就回释放。[1] 这个和C++中的shared_ptr非常类似。
  
  Rc 含有一个双重的弱引用，std::rc::Weak；这个描述了一个弱引用指针指向一个被其他Rc拥有的数据。我们可以通过Weak访问一个数据，如果只仅仅剩下弱指针则分配的内存会被丢弃。在C++这个就是weak_ptr。

- std::cell::RefCell是一个可变内存包含了动态借用检查规则。RefCell允许我们采取和绕过堆数据引用并且不使用普通的借用检查。然而，它仍然是安全的。所有的借用规则在运行时强制的使用RefCell。

这里是如何定义搜索二叉树的数据结构：

```rust
use std::cell::RefCell;
use std::rc::{Rc, Weak};

pub struct Tree {
    count: usize,
    root: Option<NodeLink>,
}

type NodeLink = Rc<RefCell<Node>>;

#[derive(Debug)]
struct Node {
    data: i32,
    left: Option<NodeLink>,
    right: Option<NodeLink>,
    parent: Option<Weak<RefCell<Node>>>,
}
```

所有权的连接被描述成Option<Rc<RefCell<Node>>>；不拥有的连接被描述成Option<Weak<RefCell<Node>>>。让我们看看代码描述:

```rust
/// Insert a new item into the tree; returns `true` if the insertion
/// happened, and `false` if the given data was already present in the
/// tree.
pub fn insert(&mut self, data: i32) -> bool {
    if let Some(root) = &self.root {
        if !self.insert_at(root, data) {
            return false;
        }
    } else {
        self.root = Some(Node::new(data));
    }
    self.count += 1;
    true
}

// Insert a new item into the subtree rooted at `atnode`.
fn insert_at(&self, atnode: &NodeLink, data: i32) -> bool {
    let mut node = atnode.borrow_mut();
    if data == node.data {
        false
    } else if data < node.data {
        match &node.left {
            None => {
                let new_node = Node::new_with_parent(data, atnode);
                node.left = Some(new_node);
                true
            }
            Some(lnode) => self.insert_at(lnode, data),
        }
    } else {
        match &node.right {
            None => {
                let new_node = Node::new_with_parent(data, atnode);
                node.right = Some(new_node);
                true
            }
            Some(rnode) => self.insert_at(rnode, data),
        }
    }
}
```

简单起见，这篇文章中的示例将操作从根节点到顶层函数/方法，称作递归的方法来操作节点层级。在这个例子中，insert_at获取连接然后插入新数据到子节点中。它保存了搜索二叉树的不变性（小的在左子树，大的在右子树）。这里每次开始都会调用borrow_mut()。它从RefCell指针中获得了atnode的可变引用。但是它不是常规的&mut 的常规引用；而是一个称作std::cell::RefMut的特别引用。这也是可变性魔法发生的地方 - 这没有显示的&mut，但是代码事实上可以操作下面的数据。

重申下，这个代码是保持安全的。如果你在RefCell上尝试做另一次borrow_mut()，在前一个RefMut还在作用域时，你将会得到一个运行时的panic。运行时会保证安全。

另外一个感兴趣的例子是私有的find_node方法，通过查找一个给定的数据来返回一个节点，代码如下：

```rust
/// Find the item in the tree; returns `true` iff the item is found.
pub fn find(&self, data: i32) -> bool {
    self.root
        .as_ref()
        .map_or(false, |root| self.find_node(root, data).is_some())
}

fn find_node(&self, fromnode: &NodeLink, data: i32) -> Option<NodeLink> {
    let node = fromnode.borrow();
    if node.data == data {
        Some(fromnode.clone())
    } else if data < node.data {
        node.left
            .as_ref()
            .and_then(|lnode| self.find_node(lnode, data))
    } else {
        node.right
            .as_ref()
            .and_then(|rnode| self.find_node(rnode, data))
    }
}
```

开始的.borrow()调用是我们如何提问RefCell来提供了一个不可变的内部数据（自然的，这个不能跟任何可变引用在运行时共存）。当我们找到了一个节点，我们clone了Rc，因为我们需要节点分开的共享所有者。这让Rust保证当返回的Rc存在的时候不能将节点丢弃掉。

正如所有的代码示例，这个方法是可行的。需要大量的练习和耐心才能保证正确，至少对于没有经验的Rust程序员来说。自从每个节点被3层间接包装(Option,Rc和RefCell)，编写代码可能有些棘手，因为在任何时候你都必须记住你“目前处于”哪个层级。

这种方法的另一个缺点是获取存储在树中的简单引用不是那么容易。就像上面例子，最顶层的find方法不能获取到节点或者它的内容，仅仅是一个boolean变量。这可不太好；例如，只能使successor方法次优。这里的问题就是通过RefCell我们不能返回一个常规的数据引用，RefCell必须保持所有的借用运行时追踪。我们仅仅能返回std::cell::Ref，但这缺漏了实现细节。这不是致命的缺陷，但在使用这些类型编写代码时需要注意一些事情。

## 将句柄放入向量来作为节点引用

第二个方法是我们前面讨论过的Tree拥有所有的节点，使用简单的Vec。然后所有的节点引用变为“句柄”--索引到这个向量中。这是数据结构：

```rust
pub struct Tree {
    // All the nodes are owned by the `nodes` vector. Throughout the code, a
    // NodeHandle value of 0 means "none".
    root: NodeHandle,
    nodes: Vec<Node>,
    count: usize,
}

type NodeHandle = usize;

#[derive(Debug)]
struct Node {
    data: i32,
    left: NodeHandle,
    right: NodeHandle,
    parent: NodeHandle,
}
```

同样的完整代码放入[GitHub](https://github.com/eliben/code-for-blog/blob/master/2021/rust-bst/src/nodehandle.rs)，这里展示突出的部分。这是插入操作：

```rust
/// Insert a new item into the tree; returns `true` if the insertion
/// happened, and `false` if the given data was already present in the
/// tree.
pub fn insert(&mut self, data: i32) -> bool {
    if self.root == 0 {
        self.root = self.alloc_node(data, 0);
    } else if !self.insert_at(self.root, data) {
        return false;
    }
    self.count += 1;
    true
}

// Insert a new item into the subtree rooted at `atnode`.
fn insert_at(&mut self, atnode: NodeHandle, data: i32) -> bool {
    if data == self.nodes[atnode].data {
        false
    } else if data < self.nodes[atnode].data {
        if self.nodes[atnode].left == 0 {
            self.nodes[atnode].left = self.alloc_node(data, atnode);
            true
        } else {
            self.insert_at(self.nodes[atnode].left, data)
        }
    } else {
        if self.nodes[atnode].right == 0 {
            self.nodes[atnode].right = self.alloc_node(data, atnode);
            true
        } else {
            self.insert_at(self.nodes[atnode].right, data)
        }
    }
}

// Allocates a new node in the tree and returns its handle.
fn alloc_node(&mut self, data: i32, parent: NodeHandle) -> NodeHandle {
    self.nodes.push(Node::new_with_parent(data, parent));
    self.nodes.len() - 1
}
```

在编写Option<Rc<RefCell<...>>>后，这个句柄方法非常简单。这里没有间接层，一个句柄就是一个索引，一个引用就是一个句柄，句柄0就是没有。

这个版本也比链表版本更有效率，这是因为有更少的堆内存分配和单个向量对缓存更友好。

也就是说，这里也有一些问题。

首先，我们将一些安全掌握在我们手中。虽然这种方法不会导致内存损坏，多次释放和悬垂指针，它可能导致运行时panic和其他问题，因为我们处理了向量的原始索引。由于bug，这些事情可能会超出向量的边界，或者指向错误的槽等等。例如，没有什么可以阻止我们在有“实时操纵”句柄的情况下修改插槽。

另外一个问题是移除树节点。现在，代码只是简单的通过不使用任何活动句柄指向它来删除一个节点。这会导致节点不能通过树的方法到达它的节点，但是并没有释放内存。事实上，这个BST实现并没有释放任何东西：

```rust
// Replaces `node` with `r` in the tree, by setting `node`'s parent's
// left/right link to `node` with a link to `r`, and setting `r`'s parent
// link to `node`'s parent.
// Note that this code doesn't actually deallocate anything. It just
// makes self.nodes[node] unused (in the sense that nothing points to
// it).
fn replace_node(&mut self, node: NodeHandle, r: NodeHandle) {
    let parent = self.nodes[node].parent;
    // Set the parent's appropriate link to `r` instead of `node`.
    if parent != 0 {
        if self.nodes[parent].left == node {
            self.nodes[parent].left = r;
        } else if self.nodes[parent].right == node {
            self.nodes[parent].right = r;
        }
    } else {
        self.root = r;
    }
    // r's parent is now node's parent.
    if r != 0 {
        self.nodes[r].parent = parent;
    }
}
```

这个明显的错误存在于世界知名软件中。最起码，这个实现可以提升到创建一个没有使用索引的“空链表”，当节点被加入可以被再次使用。一个更有野心的方法是实现一个成熟的垃圾回收器。如果你准备接受挑战，可以尝试一下;-)

## 使用原始指针和unsafe块

最后一种方法是使用原始指针和unsafe块。这个和你用C/C++实现非常类似。完整代码[在这](https://github.com/eliben/code-for-blog/blob/master/2021/rust-bst/src/unsafeall.rs)。

```rust
pub struct Tree {
    count: usize,
    root: *mut Node,
}

#[derive(Debug)]
struct Node {
    data: i32,

    // Null pointer means "None" here; right.is_null() ==> no right child, etc.
    left: *mut Node,
    right: *mut Node,
    parent: *mut Node,
}
```

注意节点连接变为*mut Node，这个原始指针指向可变的Node。使用原始指针写代码和写C代码非常类似，在使用指针分配数据、释放数据和访问数据。让我们从分配开始，这是Node的构造：

```rust
impl Node {
    fn new(data: i32) -> *mut Self {
        Box::into_raw(Box::new(Self {
            data,
            left: std::ptr::null_mut(),
            right: std::ptr::null_mut(),
            parent: std::ptr::null_mut(),
        }))
    }

    fn new_with_parent(data: i32, parent: *mut Node) -> *mut Self {
        Box::into_raw(Box::new(Self {
            data,
            left: std::ptr::null_mut(),
            right: std::ptr::null_mut(),
            parent,
        }))
    }
}
```

我发现最简单的使用原始指针分配内存的方式是使用Box::into_raw，只要我们记得不用的时候释放这段内存，它就很好用。下面是插入操作：

```rust
/// Insert a new item into the tree; returns `true` if the insertion
/// happened, and `false` if the given data was already present in the
/// tree.
pub fn insert(&mut self, data: i32) -> bool {
    if self.root.is_null() {
        self.root = Node::new(data);
    } else {
        if !insert_node(self.root, data) {
            return false;
        }
    }

    self.count += 1;
    true
}


// Inserts `data` into a new node at the `node` subtree.
fn insert_node(node: *mut Node, data: i32) -> bool {
    unsafe {
        if (*node).data == data {
            false
        } else if data < (*node).data {
            if (*node).left.is_null() {
                (*node).left = Node::new_with_parent(data, node);
                true
            } else {
                insert_node((*node).left, data)
            }
        } else {
            if (*node).right.is_null() {
                (*node).right = Node::new_with_parent(data, node);
                true
            } else {
                insert_node((*node).right, data)
            }
        }
    }
}
```

最值得注意的点就是insert_node中的unsafe块。因为这段代码操作的是原始指针因此是必须的。在Rust中可以分配指针然后不受限的传递，但是解引用需要用unsafe。

让我们看如何移除节点工作。这是replace_node，和前面使用句柄的代码类似的代码执行的是同样的任务。

```rust
// Replaces `node` with `r` in the tree, by setting `node`'s parent's
// left/right link to `node` with a link to `r`, and setting `r`'s parent
// link to the `node`'s parent. `node` cannot be null.
fn replace_node(&mut self, node: *mut Node, r: *mut Node) {
    unsafe {
        let parent = (*node).parent;
        if parent.is_null() {
            // Removing the root node.
            self.root = r;
            if !r.is_null() {
                (*r).parent = std::ptr::null_mut();
            }
        } else {
            if !r.is_null() {
                (*r).parent = parent;
            }
            if (*parent).left == node {
                (*parent).left = r;
            } else if (*parent).right == node {
                (*parent).right = r;
            }
        }
        // node is unused now, so we can deallocate it by assigning it to
        // an owning Box that will be automatically dropped.
        Box::from_raw(node);
    }
}
```

这个描述了如何使用原始指针来释放堆内存数据，通过Box::from_raw。这个使用Box来获得所有权，Box有析构函数，所以在超出作用域时会自动释放内存。

这给我们带来了一个重要的点：我们需要注意释放Tree的内存。不同于前面的方法，这里没有默认的Drop实现。因为我们的树中包含唯一的事情是root：*mut Node并且Rust不知道如何释放掉它。如果我们执行没有实现Drop trait的测试，将会有内存泄漏。这是一个简单的Drop实现来修复这个问题：

```rust
impl Drop for Tree {
    fn drop(&mut self) {
        // Probably not the most efficient way to destroy the whole tree, but
        // it's simple and it works :)
        while !self.root.is_null() {
            self.remove_node(self.root);
        }
    }
}
```

作为练习，试着实现一个更有效率的Drop。

使用原始指针编写代码显得相当自然；最终的LOC计数是相似的，比起使用Option<Rc<RefCell<Node>>>原始指针的精神负担显得更少。虽然没有对它进行基准测试，我的预感是指针版本应该更有效率。至少，它避开了RefCell所做的动态借用检查。当然，另一方面是失去安全性。使用不安全的版本，我们可能会遇到所有古老的C内存错误。

## 结论

这篇文章的目的是回顾在Rust中实现常见的链表数据结构，它涵盖了具有三个安全级别的方法：

- 使用Rc和RefCell的安全版本
- 内存安全但是更容易出现bug的利用整数句柄转换为Vec
- 使用原始指针的不安全版本

三种方法都有其各自的优点，了解一下非常有用。来自Rust的标准库和一些流行的crate用数据表明，第三种方法更受欢迎。

感谢阅读，因为这是我第一个不平凡的rust的文章。我会对任何评论、反馈或者建议感兴趣。随时给我发电子邮件或者对转发到的任何聚合站点发表评论，我会时不时关注的。
