/*
 * The MIT License
 *
 * Copyright 2018 jxpearce.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.githubautostatus;

/**
 * Class for StatsD configuration notifier.
 * @author Shane Gearon (shane.gearon@hootsuite.com)
 */
public class StatsdNotifierConfig {

    private String jobFolderPath;
    private String statsdHost;
    private String statsdPort;
    private String statsdBucket;
    private String statsdMaxSize;

    /**
     * Gets the statsDBucket.
     *
     * @return statsDBucket.
     */
    public String getJobFolderPath() {
        return jobFolderPath;
    }

    /**
     * Gets statsd url.
     *
     * @return statsd url.
     */
    public String getStatsdHost() {
        return statsdHost;
    }

    /**
     * Gets statsd port.
     *
     * @return statsd port.
     */
    public String getStatsdPort() {
        return statsdPort;
    }

    /**
     * Gets statsd bucket.
     *
     * @return statsd bucket.
     */
    public String getStatsdBucket() {
        return statsdBucket;
    }

    /**
     * Gets statsd max packet size.
     *
     * @return statsd max packet size.
     */
    public String getStatsdMaxSize() {
        return statsdMaxSize;
    }

    /**
     * Creates an statsd notification config based on the global settings.
     *
     * @param fullJobPath full folder and job path of jenkins job
     * @return config.
     */
    public static StatsdNotifierConfig fromGlobalConfig(String fullJobPath) {
        BuildStatusConfig config = BuildStatusConfig.get();

        StatsdNotifierConfig statsdNotifierConfig = new StatsdNotifierConfig();

        String jobFolderPath = fullJobPath;//sanitize function

        if (config.getEnableStatsd()) {
            statsdNotifierConfig.jobFolderPath = jobFolderPath;
            
            statsdNotifierConfig.statsdHost = config.getStatsdHost();
            statsdNotifierConfig.statsdPort = config.getStatsdPort();
            statsdNotifierConfig.statsdBucket = config.getStatsdBucket();
            statsdNotifierConfig.statsdMaxSize = config.getStatsdMaxSize();
        }

        return statsdNotifierConfig;
    }
}
