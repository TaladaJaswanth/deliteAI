/*
 * SPDX-FileCopyrightText: (C) 2025 DeliteAI Authors
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import org.gradle.api.Project
import org.gradle.api.credentials.AwsCredentials
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.credentials
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

class Publishing(
    private val project: Project,
    private val neGradleConfig: NEGradleConfig,
    private val artifact: String
) {
    fun apply() {
        project.plugins.withType<MavenPublishPlugin> {
            project.afterEvaluate {
                project.extensions.configure<PublishingExtension> {
                    publications {
                        register<MavenPublication>("internalRelease") {
                            from(project.components.getByName("internalRelease"))
                            groupId = "dev.deliteai"
                            this.artifactId = artifact
                            version = neGradleConfig.releaseVersion
                        }
                        register<MavenPublication>("externalRelease") {
                            from(project.components.getByName("externalRelease"))
                            groupId = "dev.deliteai"
                            this.artifactId = artifact
                            version = neGradleConfig.releaseVersion
                        }
                    }
                    repositories {
                        maven {
                            name = "dev"
                            url = project.uri(project.getLocalProperty("ANDROID_DEV_AWS_S3_URL"))
                            credentials(AwsCredentials::class) {
                                accessKey = project.getLocalProperty("ANDROID_DEV_AWS_ACCESS_KEY_ID")
                                secretKey = project.getLocalProperty("ANDROID_DEV_AWS_SECRET_ACCESS_KEY")
                            }
                        }
                        maven {
                            name = "prod"
                            url =
                                project.uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2/")
                            credentials {
                                username = project.getLocalProperty("OSS_USER")
                                password = project.getLocalProperty("OSS_PASSWORD")
                            }
                        }
                    }
                }
            }
        }
    }
}
