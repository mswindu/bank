package com.snilov.bank.config;

import com.snilov.bank.model.Account;
import com.snilov.bank.model.Card;
import com.snilov.bank.model.Transaction;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Account.class);
        config.exposeIdsFor(Card.class);
        config.exposeIdsFor(Transaction.class);
    }
}
