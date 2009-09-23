/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.commons.scheduler;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A scheduler to schedule time/cron based jobs.
 * A job is an object which is executed/fired by the scheduler. The object
 * should either implement the {@link Job} interface or the {@link Runnable}
 * interface.
 */
public interface Scheduler {

    /** Name of the configuration property to define the period for a job.
     * The period is expressed in seconds.*/
    String PROPERTY_SCHEDULER_PERIOD = "scheduler.period";

    /** Name of the configuration property to define the cron expression for a job. */
    String PROPERTY_SCHEDULER_EXPRESSION = "scheduler.expression";

    /** Name of the configuration property to define if the job can be run concurrently. */
    String PROPERTY_SCHEDULER_CONCURRENT = "scheduler.concurrent";

    /** Name of the configuration property to define the job name. */
    String PROPERTY_SCHEDULER_NAME = "scheduler.name";


    /**
     * Schedule a time based job.
     * Note that if a job with the same name has already beed added, the old job is cancelled and this new job replaces
     * the old job.
     *
     * @param name The name of the job - or null. If no name is specified it can't be cancelled.
     * @param job The job to execute (either {@link Job} or {@link Runnable}).
     * @param config An optional configuration object - this configuration is only passed to the job the job implements {@link Job}.
     * @param schedulingExpression The time specification using a scheduling expression.
     * @param canRunConcurrently Whether this job can run even if previous scheduled runs are still running.
     * @throws IllegalArgumentException If the scheduling expression can't be parsed or if the job has not the correct type.
     * @throws Exception If the job can't be scheduled.
     */
    void addJob(String name, Object job, Map<String, Serializable> config, String schedulingExpression, boolean canRunConcurrently)
    throws Exception;

    /**
     * Schedule a periodic job.
     * The job is started the first time when the period has passed.
     * Note that if a job with the same name has already beed added, the old job is cancelled and this new job replaces
     * the old job.
     *
     * @param name The name of the job - or null. If no name is specified it can't be cancelled.
     * @param job The job to execute (either {@link Job} or {@link Runnable}).
     * @param config An optional configuration object - this configuration is only passed to the job the job implements {@link Job}.
     * @param period Every period seconds this job is started.
     * @param canRunConcurrently Whether this job can run even if previous scheduled runs are still running.
     * @throws IllegalArgumentException If the job has not the correct type.
     * @throws Exception If the job can't be scheduled.
     */
    void addPeriodicJob(String name, Object job, Map<String, Serializable> config, long period, boolean canRunConcurrently)
    throws Exception;

    /**
     * Fire a job immediately and only once.
     *
     * @param job The job to execute (either {@link Job} or {@link Runnable}).
     * @param config An optional configuration object - this configuration is only passed to the job the job implements {@link Job}.
     * @throws IllegalArgumentException If the job has not the correct type.
     * @throws Exception If the job can't be scheduled.
     */
    void fireJob(Object job, Map<String, Serializable> config)
    throws Exception;

    /**
     * Fire a job immediately more than once.
     *
     * @param job The job to execute (either {@link Job} or {@link Runnable}).
     * @param config An optional configuration object - this configuration is only passed to the job the job implements {@link Job}.
     * @param times The number of times this job should be started (must be higher than 1)
     * @param period Every period seconds this job is started.
     * @throws IllegalArgumentException If the job has not the correct type.
     * @return true if the code could be added, false otherwise.
     * @since 2.1
     */
    boolean fireJob(Object job, Map<String, Serializable> config, int times, long period);

    /**
     * Fire a job once at a specific date
     * Note that if a job with the same name has already beed added, the old job is cancelled and this new job replaces
     * the old job.
     *
     * @param name The name of the job - or null. If no name is specified it can't be cancelled.
     * @param job The job to execute (either {@link Job} or {@link Runnable}).
     * @param config An optional configuration object - this configuration is only passed to the job the job implements {@link Job}.
     * @param date The date this job should be run.
     * @throws IllegalArgumentException If the job has not the correct type.
     * @throws Exception If the job can't be scheduled.
     */
    void fireJobAt(String name, Object job, Map<String, Serializable> config, Date date)
    throws Exception;

    /**
     * Fire a job once at a specific date, several times with a given interval.
     * Note that if a job with the same name has already beed added, the old job is cancelled and this new job replaces
     * the old job.
     *
     * @param name The name of the job - or null. If no name is specified it can't be cancelled.
     * @param job The job to execute (either {@link Job} or {@link Runnable}).
     * @param config An optional configuration object - this configuration is only passed to the job the job implements {@link Job}.
     * @param date The date this job should be run.
     * @param times The number of times this job should be started (must be higher than 1)
     * @param period Every period seconds this job is started.
     * @throws IllegalArgumentException If the job has not the correct type.
     * @return true if the code could be added, false otherwise.
     * @since 2.1
     */
    boolean fireJobAt(String name, Object job, Map<String, Serializable> config, Date date, int times, long period);

    /**
     * Remove a scheduled job by name.
     *
     * @param name The name of the job.
     * @throws NoSuchElementException If the job is not scheduled.
     */
    void removeJob(String name)
    throws NoSuchElementException;
}
