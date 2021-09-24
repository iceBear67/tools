# Tools
常用工具集合

# Showcase
```java
@Internal // 标注一个内部 API
public class Showcase {
    private int myConfigColumn;
    @Since("0.0.1") // 标注API什么时候出现
    public void myAPI(){
        // 常见的 Validation.
        Validation.isNumber("0.1","It isn't a number!");
        Validation.notNull(null,"absolutely null.");
        Validation.checkTrue(true," message ");

        // 懒加载，自带缓存的 Supplier.
        Lazy<String> lazyLoader = Lazy.by(()-> HttpRequest.get("https://sbnc.com/mySexySBNC.txt").body() ); // 对于J11以下提供了发起HTTP请求的方案（J11开始有标准库的现代HTTP CLIENT）
        lazyLoader.get();
        lazyLoader.getLocked(); // 同步获取，内部是 synchronized
        // 不使用 Levis 表达式和 Functional，判断null和返回数据
        String result;
        String msgMayNull = System.getProperty("message");
        if(msgMayNull==null){
            String body = HttpRequest.get("xxx").body();
            result = body.split(" ")[1];
        }else{
            result = msgMayNull;
        }

        // 在三元表达式内你可能需要通过一步以上的步骤来获取某些数据，这时候你可以使用 Functional
        result = msgMayNull!=null?msgMayNull:Functional.from(()->{
            String body = HttpRequest.get("xxx").body();
            return body.split(" ")[1];
        });
        // 实际上你不应该让 Functional 来做这种无聊的事情，对于判空操作，使用 Optional 得到更优雅的写法：
        result = Optional.ofNullable(msgMayNull).orElseGet(()->{
            String body = HttpRequest.get("xxx").body();
            return body.split(" ")[1];
        });

        // 停止使用 AbstractMap.SimpleEntry!
        Pair<String,String> pair = Pair.of("a","b");
        System.out.println(pair.key);
        System.out.println(pair.value);
        Triple<Integer,Integer,Integer> trip = Triple.of(1,1,1);
        Quadruple<String,String,String,String> quad = Quadruple.of("","","","");

        // Unsafe
        Unsafe.ensureClassInitialized(Showcase.class);

        // Bukkit 环境配置好的 gson（支持 ItemStack 和 Location 的序列化），需要有 gson 和 ProtocolLib 在 classpath 里。
        Util.gsonForBukkit();

        // Gson 包装，序列化读写配置
        SimpleConfig<Showcase> conf = new SimpleConfig<>(new File("."),Showcase.class);
        conf.saveDefault();
        conf.reloadConfig();
        Showcase showcase = conf.get();
        System.out.println( showcase.myConfigColumn);

    }
}

```