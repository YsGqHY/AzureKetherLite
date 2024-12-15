# AzureKetherLite
> 前往 [Actions](https://github.com/YsGqHY/AzureKetherLite/actions) 下载最新版插件
> 
> 部分动作需要使用最新版 **AzureFlow** 正常使用

## 构建发行版本

发行版本用于正常使用, 不含 TabooLib 本体。

```
./gradlew build
```

## 构建开发版本

开发版本包含 TabooLib 本体, 用于开发者使用, 但不可运行。

```
./gradlew taboolibBuildApi -PDeleteCode
```

> 参数 -PDeleteCode 表示移除所有逻辑代码以减少体积。