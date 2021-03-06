# 概述

pandas是一个Python写的可以快速、灵活的、数据结构表现力丰富的第三方库，设计之初就是为了处理关系型或标记型的数据处理工具。它的最终目标是成为一个高级的处理实际数据分析的高级积木。额外的，更广泛的目的是在任何语言中成为一个增强的，灵活的开源数据分析处理工具。它正在向这个目标努力。

pandas适合很多种数据：

- 有各种不同类型列的表格数据，就像SQL的Table或者Excel的sheet
- 有序或者无序(不是必须的固定频率)的时间统计数据
- 有行列标记的任意矩阵数据
- 其他类型的可观察/统计的数据集。实际上在pandas并不需要去标记的数据。

pandas主要有2个数据结构，**Series**(一维的)和**DataFrame**(二维的)，操作大部分典型的用于金融、统计、社会科学、工程学。对于R语言用户来说，**DataFrame**提供了所有的东西，R语言的data.frame多得多。Pandas是基于Numpy构建的包含了很多第三方库的科学计算库。

下面这些方面pandas做的都不错：

- 很好的处理有确实数据的浮点型运算
- 大小可变：可以通过DataFrame改变列的插入或者删除操作或者大对象
- 自动或者明确的数据对齐：对象可以被明确的指定按标签集合对齐，或者用户可以简单的忽视标签，根据你的计算自动对齐。
- 强大、灵活的分组功能支持数据集的分割、组合操作，同时支持聚合、转换。
- 更简单的从杂乱的、不通索引的Python或者Numpy其他数据结构转换到DataFrame。
- 智能的基于标签的切片，花式索引，大数据稽核子集操作
- 直观的合并和加入数据集
- 灵活的重塑和反转数据集
- 多级标签的数据轴
- 强大的IO工具可以从平面文件(csv)、Excel、数据库中超快的用HDF5格式导入导出数据
- 时间序列-具体功能：数据范围生成和频次转换，移动窗口统计，移动窗口线性回归，数据移动和延迟等

其中很多原则就是经常使用其他语言/科学研究环境解决这些缺点。数据科学家们一般都会把数据分为几个阶段：数据改写和清洗，分析和处理，最终汇总结果通过绘图或者表格展示出来。pandas很适合做这个工作。

其他注意点：

- pandas很快。许多底层的算法实现都被Cython重写了。然而，和其他事情一样概括起来就是牺牲性能，所以如果你的应用聚焦于某个特性你应该创建一个更快的专用工具。
- pandas依赖于statsmodels，python中很重要的统计计算生态系统
- pandas广泛的应用于金融项目。

## 数据结构

| Dimensions | Name | Description |
|---| ----- | ----- |
|1|Series|1D labeled homogeneously-typed array|
|2|DataFrame|General 2D labeled,size-mutable tabular structure with potentially heterogeneously-typed column|
