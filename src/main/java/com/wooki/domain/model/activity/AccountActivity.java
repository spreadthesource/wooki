package com.wooki.domain.model.activity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "AccountsActivities")
@PrimaryKeyJoinColumn(name = "account_activity_id")
public class AccountActivity extends Activity<AccountEventType>
{

    @Column(name = "type")
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
