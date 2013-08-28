/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.tooling.internal.provider;

import org.gradle.api.internal.GradleInternal;
import org.gradle.tooling.internal.protocol.*;
import org.gradle.tooling.internal.provider.connection.ProviderBuildResult;
import org.gradle.tooling.provider.model.ToolingModelBuilder;
import org.gradle.tooling.provider.model.ToolingModelBuilderRegistry;
import org.gradle.tooling.provider.model.UnknownModelException;

class DefaultBuildController implements InternalBuildController {
    private final GradleInternal gradle;
    private final ToolingModelBuilderRegistry modelBuilderRegistry;

    public DefaultBuildController(GradleInternal gradle, ToolingModelBuilderRegistry modelBuilderRegistry) {
        this.gradle = gradle;
        this.modelBuilderRegistry = modelBuilderRegistry;
    }

    public BuildResult<?> getBuildModel() throws BuildExceptionVersion1 {
        return new ProviderBuildResult<Object>(gradle);
    }

    public BuildResult<?> getModel(Object target, ModelIdentifier modelIdentifier) throws BuildExceptionVersion1, InternalUnsupportedModelException {
        if (target != null) {
            return null;
        }

        ToolingModelBuilder builder;
        try {
            builder = modelBuilderRegistry.getBuilder(modelIdentifier.getName());
        } catch (UnknownModelException e) {
            throw (InternalUnsupportedModelException) (new InternalUnsupportedModelException()).initCause(e);
        }
        Object model = builder.buildAll(modelIdentifier.getName(), gradle.getDefaultProject());
        return new ProviderBuildResult<Object>(model);
    }
}