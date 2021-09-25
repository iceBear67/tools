package io.ib67.util;

import java.util.function.Consumer;

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

  public CatchingContext<T> onFailure(Consumer<Throwable> consumer) {
    if (failure != null) {
      consumer.accept(failure);
    }
    return this;
  }

  public CatchingContext<T> recordError(String bottleName, String message) {
    if (failure != null) {
      ExceptionBottle.getByModule(bottleName).collect(failure, message);
    }
    return this;
  }

  public CatchingContext<T> recordError(String bottleName) {
    recordError(bottleName, "Nope");
    return this;
  }

  public CatchingContext<T> recordError() {
    recordError("GLOBAL");
    return this;
  }

  public boolean isFailed() {
    return failure != null;
  }

  public T getResult() {
    return result;
  }

  public CatchingContext<T> onSuccess(Consumer<T> consumer) {
    if (result != null) {
      consumer.accept(result);
    }
    return this;
  }
}
