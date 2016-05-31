package com.soneyu.smshub.data;

import java.util.List;

/**
 * Created by songnob on 7/24/2015.
 */
public class HubResponse extends GeneralResponse
{
    private List<HubData> data;

    public List<HubData> getData()
    {
        return data;
    }

    public static class HubData
    {
        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public String getReg_id()
        {
            return reg_id;
        }

        public void setReg_id(String reg_id)
        {
            this.reg_id = reg_id;
        }

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }

        public String getUuid()
        {
            return uuid;
        }

        public void setUuid(String uuid)
        {
            this.uuid = uuid;
        }

        private String name;
        private int id;
        private String reg_id;
        private String type;
        private String uuid;
    }
}
