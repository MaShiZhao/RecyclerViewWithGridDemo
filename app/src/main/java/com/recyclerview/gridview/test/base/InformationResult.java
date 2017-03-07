package com.recyclerview.gridview.test.base;

import java.util.List;

/**
 * Created by MaShiZhao on 16/9/20.
 */
public class InformationResult
{

    /**
     * list : [{"imgs":["http://www.cloud.com/Public/files/system/mobile_picture1445838885.jpg.w400.jpg","http://www.cloud.com/Public/files/system/mobile_picture1445838885.jpg.w400.jpg","http://www.cloud.com/Public/files/system/mobile_picture1445838885.jpg.w400.jpg"],"url":"http://tht.cloud.com/Admin/Setting/mobileAction","title":"标题","form":"来源","brief":"简介","time":"4162662626"},{"imgs":["http://www.cloud.com/Public/files/system/mobile_picture1445838885.jpg.w400.jpg"],"url":"http://tht.cloud.com/Admin/Setting/mobileAction","title":"标题","form":"来源","brief":"简介","time":"4162662626"}]
     * totalPage : 0
     */

    private DataBean data;
    /**
     * data : {"list":[{"imgs":["http://www.cloud.com/Public/files/system/mobile_picture1445838885.jpg.w400.jpg","http://www.cloud.com/Public/files/system/mobile_picture1445838885.jpg.w400.jpg","http://www.cloud.com/Public/files/system/mobile_picture1445838885.jpg.w400.jpg"],"url":"http://tht.cloud.com/Admin/Setting/mobileAction","title":"标题","form":"来源","brief":"简介","time":"4162662626"},{"imgs":["http://www.cloud.com/Public/files/system/mobile_picture1445838885.jpg.w400.jpg"],"url":"http://tht.cloud.com/Admin/Setting/mobileAction","title":"标题","form":"来源","brief":"简介","time":"4162662626"}],"totalPage":0}
     * code : 1000
     * msg : 成功
     * xhprof : no setup
     * runTm : s:1474350417.636 e:1474350417.692 tms=55ms
     * mem : 3.9 MB
     * server : wt1.nazhengshu.com
     * serverTime : 1474350417
     */

    private int code;
    private String msg;
    private String xhprof;
    private String runTm;
    private String mem;
    private String server;
    private String serverTime;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getXhprof() {
        return xhprof;
    }

    public void setXhprof(String xhprof) {
        this.xhprof = xhprof;
    }

    public String getRunTm() {
        return runTm;
    }

    public void setRunTm(String runTm) {
        this.runTm = runTm;
    }

    public String getMem() {
        return mem;
    }

    public void setMem(String mem) {
        this.mem = mem;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public static class DataBean {
        private int totalPage;
        /**
         * imgs : ["http://www.cloud.com/Public/files/system/mobile_picture1445838885.jpg.w400.jpg","http://www.cloud.com/Public/files/system/mobile_picture1445838885.jpg.w400.jpg","http://www.cloud.com/Public/files/system/mobile_picture1445838885.jpg.w400.jpg"]
         * url : http://tht.cloud.com/Admin/Setting/mobileAction
         * title : 标题
         * form : 来源
         * brief : 简介
         * time : 4162662626
         */

        private List<ListBean> list;

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String id;
            private String sourceUrl;
            private String title;
            private String source;
            private String brief;
            private String time;
            private List<String> imgs;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSourceUrl() {
                return sourceUrl;
            }

            public void setSourceUrl(String url) {
                this.sourceUrl = url;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getBrief() {
                return brief;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public List<String> getImgs() {
                return imgs;
            }

            public void setImgs(List<String> imgs) {
                this.imgs = imgs;
            }
        }
    }
}
