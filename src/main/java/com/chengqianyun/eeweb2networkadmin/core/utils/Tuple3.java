package com.chengqianyun.eeweb2networkadmin.core.utils;


import java.io.Serializable;
import lombok.Data;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/4/25
 */
@Data
public class Tuple3<T1, T2,T3> implements Serializable {

  private T1 t1;
  private T2 t2;
  private T3 t3;

  public Tuple3(T1 t1, T2 t2, T3 t3) {
    this.t1 = t1;
    this.t2 = t2;
    this.t3 = t3;
  }

  public Tuple3() {
  }
}
