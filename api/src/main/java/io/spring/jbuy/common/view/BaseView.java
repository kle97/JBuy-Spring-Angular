package io.spring.jbuy.common.view;

public class BaseView {

    public interface Create extends Update {
    }

    public interface Update {
    }

    public interface Admin extends Public {
    }

    public interface Public {
    }

}
