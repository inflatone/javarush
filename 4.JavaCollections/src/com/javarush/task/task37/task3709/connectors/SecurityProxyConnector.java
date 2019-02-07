package com.javarush.task.task37.task3709.connectors;

import com.javarush.task.task37.task3709.security.SecurityChecker;
import com.javarush.task.task37.task3709.security.SecurityCheckerImpl;

public class SecurityProxyConnector implements Connector {
    private SecurityChecker checker;
    private SimpleConnector original;

    public SecurityProxyConnector(String s) {
        original = new SimpleConnector(s);
        checker = new SecurityCheckerImpl();
    }

    @Override
    public void connect() {
        if (checker.performSecurityCheck()) {
            original.connect();
        }
    }
}
