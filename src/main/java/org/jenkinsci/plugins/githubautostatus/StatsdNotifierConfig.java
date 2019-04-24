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
import java.util.logging.Level;
import java.util.logging.Logger;

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
     * Gets the full folder path of a jenkins job.
     *
     * @return full folder path of jenkins job.
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
     * Returns the statsd including the global prefix up to the branch bucket
     * 
     * @param fullJobPath full folder and job path of jenkins job
     * @return string of path up to branch bucket
     */
    public String getBranchPath(String fullJobPath) {
        String sanitizedFolderPath = sanitizeAll(fullJobPath);
        return String.format("pipeline.%s", sanitizedFolderPath);
    }

        /**
     * Applies all sanitizations to a key, folders are expanded into seperate statsd buckets.
     * It firest applies bucket sanitization (removing periods to prevent them being interprested as 
     * seperate buckets). It the applies the statsd bucket key sanitization.
     * 
     * @param key key to sanitize
     * @return sanitized key
     */
    public String sanitizeAll(String key) {
        return collapseEmptyBuckets(statsdSanitizeKey(sanitizeKey(key)));
    }

     /**
     * Does the same sanitization as Statsd would do if sanitization is on.
     * See: https://github.com/statsd/statsd/blob/master/stats.js#L168
     * 
     * @param key key to sanitize
     * @return santized key
     */
    private String statsdSanitizeKey(String key) {
        return key.replaceAll("\\s+", "_").replaceAll("/", ".").replaceAll("[^a-zA-Z_\\-0-9\\.]", "");
    }

    /**
     * Collapses empty buckets into a single period.
     * 
     * @param key key to sanitize
     * @return sanitized key
     */
    private String collapseEmptyBuckets(String key) {
        return key.replaceAll("\\.{2,}", ".");
    }

    /**
     * Does Jenkins specific key sanitization.
     * 
     * @param key key to sanitize
     * @return sanitized key
     */
    private String sanitizeKey(String key) {
        return key.replaceAll("\\.", "");
    }

    private static final Logger LOGGER = Logger.getLogger("jenkins.StatsdNotifierConfig");


    /**
     * Creates an statsd notification config based on the global settings.
     *
     * @param fullJobPath full folder and job path of jenkins job
     * @return config.
     */
    public static StatsdNotifierConfig fromGlobalConfig(String fullJobPath) {
        BuildStatusConfig config = BuildStatusConfig.get();

        StatsdNotifierConfig statsdNotifierConfig = new StatsdNotifierConfig();

        String jobFolderPath = statsdNotifierConfig.getBranchPath(fullJobPath);//sanitize function

        // Can't seem to print this log out into Jenkins console output
        LOGGER.log(Level.INFO, " The sanitized jobFolderPath is = " + jobFolderPath);

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
