package az.abb.contentful.to.strapi.migration.client.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class StrapiRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization",
                "Bearer " +
                        "27d903479d9aafdbec4c9246825849f7bb252d4f350cad6837d25787f6043103b7a3112551e64ff9d43058447e6ee544ccd5e29c564b043cab6afd875332f3b86f9ef1004fb26c48ae651e8f37ba96d0e0af4f9b448ae4517d2064e4caf28d2c9aac32742d7e55ede69c6bffa6e3c869ac06205b55c35a4f3dc7de60447b74fe");
    }

}
