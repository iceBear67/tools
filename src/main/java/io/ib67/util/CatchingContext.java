package io.ib67.util;

import lombok.Builder;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Builder
public class CatchingContext<T> {
  private Throwable failure;
  private T result;
  public CatchingContext(T result){
    this.result=result;
  }
  public CatchingContext(Throwable failure){
    this.failure=failure;
  }
  public CatchingContext<T> alsoPrintStack(){
    if(failure!=null){
      failure.printStackTrace();
    }
    return this;
  }
  public CatchingContext<T> onFailure(Consumer<Throwable> consumer){
    if(failure!=null){
      consumer.accept(failure);
    }
    return this;
  }
  public CatchingContext<T> onSuccess(Consumer<T> consumer){
    if(result!=null){
      consumer.accept(result);
    }
    return this;
  }
}
