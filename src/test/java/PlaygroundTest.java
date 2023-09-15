import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlaygroundTest {


    @Test
    public void test0() {
        Integer a = 127;
        Integer b = 127;
        assertThat(a == b).isTrue();
    }

    @Test
    public void test1() {
        Integer a = 233;
        Integer b = 233;
        assertThat(a == b).isTrue();
    }


    @Test
    public void test3() {
        assertThat(128).isSameAs(128);
    }

    @Test
    public void test4() {
        Double a = 0.8;
        Double b = 0.7;
        assertThat(a - b).isEqualTo(0.1);
    }
}
