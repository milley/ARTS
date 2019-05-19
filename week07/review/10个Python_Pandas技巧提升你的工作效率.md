# 10个Python Pandas技巧提升你的工作效率

[10 Python Pandas tricks that make your work more efficient](https://link.medium.com/aabnxTebOW)

在Python的数据处理中Pandas经常被用到。有很多不错的教程，但是在这里我要介绍一些读者可能不知道的有趣的技巧。

## read_csv

很多人知道这个命令。但是如果你要读取的文件非常大，试着加上:nrows=5这个参数可以在整个表格被加载出来前只读取很少一部分。因此你可以在选择了错误的界定符时避开错误。

(或者，你可以使用linux的'head'命令来取出前5行: head -n 5 data.txt)

因此，你可以使用df.columns.tolist()提取列数据，然后增加usecols=['c1','c2',...]参数来加载你需要的列。还有，如果你知道一些列的数据类型，你可以增加参数dbtype={'c1':str,'c2':int...}来提高加载速度。对于这个参数的另一个高级用法是，如果一列既有字符串又有数字，定义为字符串是一个最佳实践，这样当你使用这一列合并表格时能避免错误。

## select_dtypes

如果使用Python来做数据预处理，这个命令会节省你不少时间。在读入到表之后，每一列的默认的数据类型可能是bool,int64,float64,object,category,timedelta64或者datetime64。你可以首先用这个命令来检测类型分布:

> df.types.value_count()

然后使用下面的方法来选择一个子数据结构

> df.select_dtypes(include=['float64','int64'])

## copy

如果你没听过这是一个非常重要的命令。如果你运行下面的命令：

```python
import pandas as pd
df1 = pd.DataFrame({'a':[0,0,0], 'b':[1,1,1]})
df2 = df1
df2['a'] = df2['a'] + 1
df1.head()
```

你将会发现df1会被改变。这是因为df2=df1不是复制了df1来指向df2，而是设置了一个指针指向df1。所以df2的所有修改都会反映到df1上。如果要解决这个问题，可以这样

```python
df2 = df1.copy()
```

或者

```python
from copy import deepcopy
df2 = deepcopy(df1)
```

## map

在做简单的数据转换时这是一个很酷的命令。你可以使用'keys'先定义一个字典然后使用旧值来产生新值的映射，例如

```python
level_map = {1: 'high', 2: 'medium', 3: 'low'}
df['c_level'] = df['c'].map(level_map)
```

其他例子：True,False转换成1，0。

## apply or not apply?

如果我们要使用一些列来产生一个新列，apply函数将会非常有用:

```python
def rule(x, y):
    if x == 'high' and y > 10:
        return 1
    else:
        return 0

df = pd.DataFrame({'c1': ['high', 'high', 'low', 'low'], 'c2': [0, 23, 17, 4]})
df['new'] = df.apply(lambda x: rule(x['c1'], x['c2']), axis = 1)
df.head()
```

上面的示例，我们定义了一个有2个输入变量的函数，然后使用apply函数接收它为列'c1'和'c2'。

但是有时候'apply'的问题是它太慢了。如果你需要计算两个列'c1'和'c2'的最大值，你可以这样使用

```python
df['maximum'] = df.apply(lambda x: max(x['c1'], x['c2']), axis=1)
```

但是你会发现它比下面这个还是慢

```python
df['maximum'] = df[['c1','c2']].max(axis=1)
```

要点：如果有其他方法能代替apply那么就不要使用apply。例如你要把列'c'取整，可以使用round(df['c'], 0)或者df['c'].round(0)来代替下面的写法：

```python
df.apply(lambda x: round(x['c'], 0), axis = 1)
```

## value counts

这个命令来检测数据的分布情况。举例来说，如果需要检查列'c'中哪些是可能值和出现的频率可以使用：

```python
df['c'].value_counts()
```

这里有一些有用的技巧/参数：

- A. normalize = True: 检查频率替代个数
- B. dropna = False: 包含空值
- C. df['c'].value_counts().reset_index(): 将统计表加入到pandas dataframe并且操作它
- D. df['c'].value_counts().reset_index().sort_values(by='index'): 使用列c对统计表来做排序

(Update2019.4.18--对于以上D，有一个更简单的:df['c'].value_counts().sort_index())

## number of missing values

当构建模型的时候，你可能需要排除一些没有值的行，你可以使用.isnull()和.sum()来计算缺少数据的行数

```python
import pandas as pd
import numpy as np
df = pd.DataFrame({'id': [1, 2, 3], 'c1': [0, 0, np.nan], 'c2': [np.nan, 1, 1]})
df = df[['id', 'c1', 'c2']]
df['num_nulls'] = df[['c1', 'c2']].isnull().sum(asix=1)
df.head()
```

## select rows with specific IDs

在SQL中我们可以使用SELECT * FROM ... WHERE ID in ('A001', 'C022',...)来获取记录。如果你想在pandas这样用：

```python
df_filter = df['ID'].isin(['A001', 'C022',...])
df[df_filter]
```

## Percentile groups

你有一个数字列，然后打算用数值来分组，比如前5%放到第一组，5-20%放到第二组，20%-50%放到第三组，最后的放到第四组。当然你可以使用pandas.cut，但是这里我想推荐另外一个：

```python
import numpy as np
cut_points = [np.percentitle(df['c'], i) for i in [50, 80, 95]]
df['group'] = 1
for i in range(3):
    df['group'] = df['group'] + (df['c'] < cut_points[i])
# or <= cut_points[i]
```

## to_csv

这个命令大家都在用。这里说2个技巧。第一个就是：

> print(df[:5].to_csv())

使用这个命令可以打印出前5行需要写入文件的数据。

另一个技巧是处理integer和缺少值混合的场景。如果一列既有数字又有缺值，数据类型将会用float代替int。当你导出表时，你可以增加float_format='%.0f'来将float取整到integers。
