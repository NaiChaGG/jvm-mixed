/*
 * Copyright 2021 JoJo Wang , homepage: https://github.com/jojoti/experiment-jvm.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.trapspring.datajdbc;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.JdbcRepositoryConfigExtension;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;

/**
 * @author JoJo Wang
 * @link github.com/jojoti
 */
//@Configuration(proxyBeanMethods = false)
// 这样声明会覆盖 spring boot 的自动加载
// 	@Configuration(proxyBeanMethods = false)
//	@ConditionalOnMissingBean(JdbcRepositoryConfigExtension.class)
//	@Import(JdbcRepositoriesRegistrar.class)
public class DisableFeaturesJdbcRepositoryConfigExtension extends JdbcRepositoryConfigExtension {

    // copy of AnnotationRepositoryConfigurationSource
    private static final String REPOSITORY_BASE_CLASS = "repositoryBaseClass";

    public DisableFeaturesJdbcRepositoryConfigExtension() {

    }

    // 参考 父类 postProcess(BeanDefinitionBuilder builder, RepositoryConfigurationSource source)
    @Override
    public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {
        builder.addPropertyReference(REPOSITORY_BASE_CLASS, OverrideSimpleJdbcRepository.class.getName());
    }

}
