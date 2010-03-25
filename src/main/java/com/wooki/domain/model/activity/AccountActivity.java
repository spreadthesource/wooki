package com.wooki.domain.model.activity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "ACCOUNT_ACTIVITY_ID")
public class AccountActivity extends Activity
{

    private AccountEventType type;

    public AccountEventType getType()
    {
        return type;
    }

    public void setType(AccountEventType type)
    {
        this.type = type;
    }

}
