# 我漂亮的Linux开发环境

[My beautiful Linux development environment](https://deepu.tech/my-beautiful-linux-development-environment/)

我常常在会议之后被问到的不是我提出的问题，而是关于我的开发环境。比起我做的的演示文稿，人们更关心我的漂亮的发行版。

我不是在抱怨，而是非常喜欢我的桌面环境。我非常喜欢它以至于害怕独自一个人时拿到了一台新的PC。我害怕会把事情搞砸（过去我搞过很多次，我想linux用户更能理解我说的）。

所以我决定向对linux感兴趣的人提供我使用的发行版最重要的方方面面。

这不仅仅是我的工作笔记本，这也是我做下面事情的主要机器。

- Java, JS, TS, Go, Python & web development
- JHipster development
- Running multiple web applications locally
- Running Docker containers
- VirtualBox for Windows testing & other VM stuff
- Kubernetes, Terraform, CloudFormation development and deployments
- Azure, AWS & GCP deployments using required CLI tools
- Heavy browser usage
- Email, chat & video conferencing
- Plex media server
- Blogging
- Youtube & Social media

## 机器配置

对所有的开发者来说机器的配置也非常的重要。我的笔记本是Dell Precision 5530移动工作站。我有一个和这个配置很接近的老笔记本Dell 5510。我在两年前将其作为了备用笔记本，但它仍然可以和现在大多数高端笔记本有一拼。

在一些时候我使用戴尔的自定义配置来获得更好的设置。它不便宜但是我的公司，XebiaLabs，提供了充足的预算，我认为也值这么多钱。这个在我看来，是为开发者打造的最好的笔记本。这是它的配置：

Processor: Intel® Core™ i9-8950HK CPU @ 2.90GHz × 12

Memory: 32GB, DDR4-2666MHz SDRAM, 2 DIMMS, Non-ECC

HDD: M.2 1TB NVMe PCIe SED class 40 SSD

Graphics: NVIDIA Quadro P2000 with 4 GB GDDR5 memory & Intel® UHD Graphics 630 (Coffeelake 3x8 GT2)

Wireless: Intel Wifi Link 9260 2x2 802.11AC + BT 4.2 vPro wireless card

Keyboard: English QWERTY US, backlit

Display: 15.6” FHD 1920x1080 Anti-Glare LED-backlit Non-touch IPS UltraSharp™

Battery: 6-cell (97Wh) Lithium-Ion battery with ExpressCharge™

## 操作系统和桌面环境

最重要的，操作系统。我运行Fedora 30和GNOME 3.32.2，我非常喜欢这个组合。我发现比起其他发行版Fedora更适合开发者，它有一个短的发布期也更稳定，因此你随时可以获取到最新的稳定软件。

桌面系统拥有好的主题不错对吧？GNOME对于主题来说很合适，我也使用Arc-Flatabulous主题并且从未更换过。我使用Paper的图标主题。

当然，它不会完全没有一些不错的GNOME插件。下面是我使用的：

- Dash to Dock
- Always Zoom Workspaces
- Auto Move Windows
- Native Window Placement
- Launch new instance
- Steal My Focus
- AlternateTab
- Window List
- Applications Menu
- Caffeine
- Clipboard Indicator
- Gistnotes
- OpenWeather
- Places Status Indicator
- System-monitor
- Todo.txt
- TopIcons Plus
- User Themes

## 开发环境

现在这些是客观的选择，只要你自己觉得适合使用其他都不重要。下面是我开发中一些重要分类的选择。我没有包含一些明显的比如Git,Vim,NodeJS,Docker,Kubernates等等。

Shell: 这个是对于开发者非常重要的。我使用Oh My ZSH包装过的ZSH。现在有一些很不错的插件和主题。我使用powerlevel9k主题，也使用zsh-autosuggestions, git, docker, docker-compose, autojump, zsh-syntax-highlighting, dnf, 和 npm 插件。更新：评论中说powerlevel10k 这个插件非常不错，我试用了之后确实比powerlevel9k更快。所以我打算将其作为主题。

Terminal: 不带良好的终端使用shell多么糟糕？幸好我们有Tilix，一个最好的终端应用程序。它有工作区、TAB页、窗口分隔、Quake模式等等。

IDE: IntelliJ IDEA Ultimate，用来开发Java和其他的JVM语言。

Code Editors: Visual Studio Code，我喜欢它，用来开发web开发，Go，Python，JS开发，DevOps，和其他相比JVM语言的一切。一个没有好的插件的VSCode是不完整的。

另外一个我使用的软件GitKraken用来管理Git repo，Beyond Compare用来对比代码，VirtualBox创建虚拟机，NVM管理NodeJS版本，SDKMan管理JDK版本。

## 生产工具

浏览器: 主要使用Google Chrome，偶尔也使用Firefox & Opera。

Email: 使用Mailspring作为我的邮件客户端。

Office: 经常使用Google Docs & Microsoft office online，但是有时候需要在本地操作就使用LibreOffice，可以很好的处理Microsoft Office & Keynote formats。

## 社交

当然会使用Slack，视频会议使用BlueJeans。

## 截图

使用Peek来做屏幕录制，Shutter用来截屏。

## 总结

有很多小的但是很有用的工具，大多数是命令行工具。有些经常提到的比如Timeshift，可以很好的备份你的机器。

当然，Linux世界中没有所有事情都完美，其他的OS也一样。在切换到Linux前我有很长时间工作在Windows下。就像所有linux用户一样，我也时不时会把环境搞砸。在Linux世界中有很多奇怪的事情，但是没有可以难倒我的。下面的一些最烦人的问题，在过去曾经是，现在也是，我不在注意任何的issure。

- 切换应用时滚动位置会跳动 - 升级到Fedora 30之后已经修复
- 休眠状态坏掉 - 升级到Fedora 30之后已经修复
- 当插入耳机，音频输出会被打破 - Fedora 28之后修复

希望以上能对大家有用！
