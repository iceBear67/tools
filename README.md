# Tools
常用工具集合

# Getting Started
```groovy
repositories {
    //...
    maven { url 'https://jitpack.io' }
}
dependencies {
    // ...
    implementation 'com.github.iceBear67:tools:Tag'
    // ...
}
```
# Showcase
```java
@Internal // 标注一个内部 API
public class Showcase {
    private int myConfigColumn;
    @Since("0.0.1") // 标注API什么时候出现
    public void myAPI(){
        // 懒加载，自带缓存的 Supplier.
        Lazy<?,String> lazyLoader = Lazy.by(()-> System.getProperty("a") );
        Lazy<String,String> lazyFunction = Lazy.by( param -> {/* ... */} );
        lazyLoader.get();
        lazyLoader.getLocked(); // 同步获取，内部是 synchronized
        lazyLoader.get(null);
        
        // 染色 String,未来可能会添加渐变等功能，然而大多数情况还是可以考虑直接replaceAll的
        String s = "" + new ColoredString("&baa&aaa").append("&bhi"); // or toString 
        
        // Logger.
        Log.info("test"); // 控制台输出: [%插件名%] XXX，注意 relocate.
        
        // 在三元表达式内你可能需要通过一步以上的步骤来获取某些数据，这时候你可以使用 Functional
        // 也可以直接 ((Supplier)()->{}).get() 但是我觉得比较丑
        result = msgMayNull!=null?msgMayNull:Functional.from(()->{
            String body = HttpRequest.get("xxx").body();
            return body.split(" ")[1];
        });
        // 实际上不应该让 Functional 来做这种无聊的事情，对于判空操作，使用 Optional 得到更优雅的写法：
        result = Optional.ofNullable(msgMayNull).orElseGet(()->{
            String body = HttpRequest.get("xxx").body();
            return body.split(" ")[1];
        });

        // DONT use AbstractMap.SimpleEntry!
        Pair<String,String> pair = Pair.of("a","b");
        System.out.println(pair.key);
        System.out.println(pair.value);
        Triple<Integer,Integer,Integer> trip = Triple.of(1,1,1);
        Quadruple<String,String,String,String> quad = Quadruple.of("","","","");

        // Unsafe
        Unsafe.ensureClassInitialized(Showcase.class);
        
        // Eval
        double d = Eval.eval("rand(0,200)+114514");
        
        // AccessibleField,支持通过 getter/setter，unsafe 获取/修改数据。
        AccessibleField f = new AccessibleField(XX.class,"myField");

        
        // Bukkit 环境配置好的 gson（支持 ItemStack 和 Location 的序列化），需要有 gson 和 ProtocolLib 在 classpath 里。
        Util.gsonForBukkit();

        // Gson 包装，序列化读写配置
        SimpleConfig<Showcase> conf = new SimpleConfig<>(new File("."),Showcase.class);
        conf.saveDefault();
        conf.reloadConfig();
        conf.setConfFileName("..."); // optional
        Showcase showcase = conf.get();
        System.out.println(showcase.myConfigColumn);

        String s = Util.readAll(InputStream); // 不解释

        // I DONT CARE WHAT THE EXCEPTION IS
        boolean failed = runCatching(() -> {
            if (true) throw new NullPointerException("sbnc");
            return "";
        }).onSuccess(t -> {
            t.equals("");
        }).onFailure(throwable -> {
        }).alsoPrintStack().isFailed();

        // 错误报告收集器
        ExceptionBottle.getDefaultBottle().collect(Throwable, Message);

        // 配合 runCatching
        runCatching(() -> {/*...*/}).recordError();

        // 用来对付泛型容器之间转换的 Box
        XX<String> xxa = Box.of(new XX<Integer>()).castTo(new XX<String>());

        // 冷却
        Cooldown cd = Cooldown.of(1000L); // 1s
        boolean succeed = cd.refresh(); //刷新
        cd.isAvailable();
        cd.waitFor(); // 自旋锁
        cd.breaks(); // 立即可用

        Util.ofNMSClass("PackeyPlayOutChat");
    }
}

```