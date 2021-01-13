package com.atguigu.gmall.search.test;

import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.dubbo.config.annotation.DubboReference;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class test {
    @DubboReference(version = "111",interfaceClass = SkuService.class)
    SkuService skuService;

    @Autowired
    JestClient jestClient;


    @RequestMapping("hhh2")
    @ResponseBody
    public void contextLoads2() throws IOException {
        //jest的dsl工具
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        //filter
//        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("productId","43");
//        boolQueryBuilder.filter(termQueryBuilder);

//        TermsQueryBuilder termsQueryBuilder = new TermsQueryBuilder("","");
//        boolQueryBuilder.filter(termsQueryBuilder);

        //must
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName","华为");
        boolQueryBuilder.must(matchQueryBuilder);

        //qurery
        searchSourceBuilder.query(boolQueryBuilder);

        //from
        searchSourceBuilder.from(0);

        //size
        searchSourceBuilder.size(20);

        //highlight
        searchSourceBuilder.highlight(null);

        String dslStr = searchSourceBuilder.toString();

        System.err.println(dslStr);

        //用API执行复杂查询
        List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();

        Search search = new Search.Builder(dslStr).addIndex("gmall0105").addType("pmsSkuInfo").build();

        SearchResult execute = jestClient.execute(search);

        List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = execute.getHits(PmsSearchSkuInfo.class);

        for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
            PmsSearchSkuInfo source = hit.source;

            pmsSearchSkuInfos.add(source);
        }

        System.out.println(pmsSearchSkuInfos.size());
    }





      @RequestMapping("hhh")
      @ResponseBody
     public String contextLoads() throws IOException {
          //查询mysql数据
          List<PmsSkuInfo> pmsSkuInfoList = new ArrayList<>();
          pmsSkuInfoList = skuService.getAllSku("61");


          //转化为es的数据结构
          List<PmsSearchSkuInfo> pmsSearchSkuInfos = new ArrayList<>();
          for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
              PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
              BeanUtils.copyProperties(pmsSkuInfo, pmsSearchSkuInfo);
              pmsSearchSkuInfos.add(pmsSearchSkuInfo);
          }

          //导入es
          for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfos) {
                                         //数据  jest技术   //p
              Index put = new Index.Builder(pmsSearchSkuInfo).index("gmall0105").type("pmsSkuInfo").id(pmsSearchSkuInfo.getId()).build();
             //http交互



               DocumentResult c = jestClient.execute(put);
               System.out.println(c.toString());
          }

      return "love";
    }
}
