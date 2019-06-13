package ru.dsoccer1980;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class JsonImplTest {

    @Test
    public void test() throws IllegalAccessException {
        JsonImpl jsonImpl = new JsonImpl();
        Gson gson = new Gson();

        MyObject myObject = new MyObject(1, "2", true, new int[]{41, 45, 46}, List.of("str1", "str2"));
        String json = jsonImpl.toJson(myObject);
        MyObject obj2 = gson.fromJson(json, MyObject.class);

        assertThat(myObject).isEqualTo(obj2);

        assertThat(jsonImpl.toJson(null)).isEqualTo(gson.toJson(null));

        assertThat(jsonImpl.toJson((byte) 1)).isEqualTo(gson.toJson((byte) 1));

        assertThat(jsonImpl.toJson((short) 1f)).isEqualTo(gson.toJson((short) 1f));

        assertThat(jsonImpl.toJson(1)).isEqualTo(gson.toJson(1));

        assertThat(jsonImpl.toJson(1L)).isEqualTo(gson.toJson(1L));

        assertThat(jsonImpl.toJson(1f)).isEqualTo(gson.toJson(1f));

        assertThat(jsonImpl.toJson(1d)).isEqualTo(gson.toJson(1d));

        assertThat(jsonImpl.toJson("aaa")).isEqualTo(gson.toJson("aaa"));

        assertThat(jsonImpl.toJson('a')).isEqualTo(gson.toJson('a'));

        assertThat(jsonImpl.toJson(new int[]{1, 2, 3})).isEqualTo(gson.toJson(new int[]{1, 2, 3}));

        assertThat(jsonImpl.toJson(new String[]{"1", "2", "3"})).isEqualTo(gson.toJson(new String[]{"1", "2", "3"}));

        assertThat(jsonImpl.toJson(List.of(1, 2, 3))).isEqualTo(gson.toJson(List.of(1, 2, 3)));

        assertThat(jsonImpl.toJson(List.of("1", "2", "3"))).isEqualTo(gson.toJson(List.of("1", "2", "3")));

        assertThat(jsonImpl.toJson(Collections.singletonList(1))).isEqualTo(gson.toJson(Collections.singletonList(1)));
    }

}




