package com.snilov.bank.config;

import com.snilov.bank.entity.Account;
import com.snilov.bank.entity.Card;
import com.snilov.bank.entity.Transaction;
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
