github.dismiss_out_of_range_messages

# for PR
if github.pr_title.include? "[WIP]" || github.pr_labels.include?("WIP")
  warn("PR is classed as Work in Progress")
end

# Warn when there is a big PR
warn("a large PR") if git.lines_of_code > 300

# Warn when PR has no assignees
warn "This PR does not have any assignees yet." unless github.pr_json["assignee"]

# --------------------
# ktlint
# --------------------
# checkstyle_format.base_path = Dir.pwd
# checkstyle_format.report "app/build/reports/ktlint/ktlint-#{ENV['APP_BUILD_TYPE'].downcase}.xml"

# --------------------
# Android Lint
# --------------------
Dir["*/build/reports/lint-results*.xml"].each do |file|
    android_lint.skip_gradle_task = true
    android_lint.report_file = file
    android_lint.lint(inline_mode: true)
end