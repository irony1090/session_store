package prv.irony.java_api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IronySession {
  /** req header에서 참조할 키 이름
     * 없을 경우 RSV에서 지정하기
     * @return
     */
    public String value() default "";

    /**
     * 세션이 존재하지 않으면 생성할 것인가의 여부
     */
    public boolean create() default false;

    /**
     * true일 경우 세션이 존재하지 않으면 엑션 발생 EXCssesion.class
     * @return
     */
    public boolean requeired() default false;

    /**
     * 빠른 검색을 위해서
     * @return
     */
    public int key_minLen() default 128;

    /**
     * UNIT : SECONDS
     * @return
     */
    public long extendExpire() default 0;
}
