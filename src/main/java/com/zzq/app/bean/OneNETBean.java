package com.zzq.app.bean;

import java.util.List;

public class OneNETBean {
    /**
     * errno : 0
     * data : {"count":4,"datastreams":[{"datapoints":[{"at":"2019-05-15 18:41:10.568","value":22.31}],"id":"temp"},{"datapoints":[{"at":"2019-05-15 18:41:10.585","value":8}],"id":"light"},{"datapoints":[{"at":"2019-05-15 18:41:10.593","value":311}],"id":"CO2"},{"datapoints":[{"at":"2019-05-15 18:41:10.577","value":88.07}],"id":"humi"}]}
     * error : succ
     */

    private int errno;
    private DataBean data;
    private String error;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static class DataBean {
        /**
         * count : 4
         * datastreams : [{"datapoints":[{"at":"2019-05-15 18:41:10.568","value":22.31}],"id":"temp"},{"datapoints":[{"at":"2019-05-15 18:41:10.585","value":8}],"id":"light"},{"datapoints":[{"at":"2019-05-15 18:41:10.593","value":311}],"id":"CO2"},{"datapoints":[{"at":"2019-05-15 18:41:10.577","value":88.07}],"id":"humi"}]
         */

        private int count;
        private List<DatastreamsBean> datastreams;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<DatastreamsBean> getDatastreams() {
            return datastreams;
        }

        public void setDatastreams(List<DatastreamsBean> datastreams) {
            this.datastreams = datastreams;
        }

        public static class DatastreamsBean {
            /**
             * datapoints : [{"at":"2019-05-15 18:41:10.568","value":22.31}]
             * id : temp
             */

            private String id;
            private List<DatapointsBean> datapoints;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public List<DatapointsBean> getDatapoints() {
                return datapoints;
            }

            public void setDatapoints(List<DatapointsBean> datapoints) {
                this.datapoints = datapoints;
            }

            public static class DatapointsBean {
                /**
                 * at : 2019-05-15 18:41:10.568
                 * value : 22.31
                 */

                private String at;
                private double value;

                public String getAt() {
                    return at;
                }

                public void setAt(String at) {
                    this.at = at;
                }

                public double getValue() {
                    return value;
                }

                public void setValue(double value) {
                    this.value = value;
                }
            }
        }
    }
}
