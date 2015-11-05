[@ww.label name='com.trusolve.scriptengine.scripttype' label='Script Type' /]

[@ww.checkbox label='Run On Server' name='com_trusolve_scriptengine_runOnServer' /]

[@ui.bambooSection dependsOn='com.trusolve.scriptengine.scriptlocation' showOn='FILE']
    [@ww.label label='Script File' name='com.trusolve.scriptengine.scriptfile' /]
[/@ui.bambooSection]

[@ui.bambooSection dependsOn='com.trusolve.scriptengine.scriptlocation' showOn='INLINE']
	[@ww.label label='Script Body' name='com.trusolve.scriptengine.scriptbody' /]
[/@ui.bambooSection]