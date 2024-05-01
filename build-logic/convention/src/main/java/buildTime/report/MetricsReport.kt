package buildTime.report

import buildTime.InMemoryReport

interface MetricsReport {
    val report: InMemoryReport
}