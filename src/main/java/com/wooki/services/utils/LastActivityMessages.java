//
// Copyright 2009 Robin Komiwes, Bruno Verachten, Christophe Cordenier
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.wooki.services.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry5.ioc.Messages;

/**
 * Utility class used to display elements in the last activity window.
 * 
 * @author ccordenier
 */
public class LastActivityMessages
{

    /**
     * Return a string (human) representation of the period since the last activity.
     * 
     * @param lastActivity
     * @return
     */
    public static String getActivityPeriod(long lastActivity, Messages messages)
    {

        String result = "";
        DateFormat formatter = new SimpleDateFormat(messages.get("date"));

        long now = System.currentTimeMillis();

        long period = (now - lastActivity) / 1000;

        if (period < 60)
        {
            result = messages.get("few-seconds-ago");
        }
        else if ((period / 60) < 60)
        {
            result = String.format(messages.get("minutes-ago"), (period / 60));
        }
        else if ((period / 60 / 60) < 24)
        {
            result = String.format(messages.get("hours-ago"), (period / 60 / 60));
        }
        else if ((period / 60 / 60 / 24) < 5)
        {
            result = String.format(messages.get("days-ago"), (period / 60 / 60 / 24));
        }
        else
        {
            result = formatter.format(new Date(lastActivity));
        }

        return result;
    }

}
