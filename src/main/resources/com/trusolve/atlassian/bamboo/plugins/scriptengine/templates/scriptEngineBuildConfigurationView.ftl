<h3>Post Build Server Script Execution</h3>
[@ww.label name='com.trusolve.scriptengine.buildprocessorserverenable' label='Enable Build Label Creation / Expiration' /]
[@ui.bambooSection dependsOn='com.trusolve.scriptengine.buildprocessorserverenable' showOn='true']
	[@ww.label name='com.trusolve.scriptengine.scripttype' label='Script Type' /]

	[@ui.bambooSection dependsOn='com.trusolve.scriptengine.scriptlocation' showOn='FILE']
	    [@ww.label label='Script File' name='com.trusolve.scriptengine.scriptfile' /]
	[/@ui.bambooSection]

	[@ui.bambooSection dependsOn='com.trusolve.scriptengine.scriptlocation' showOn='INLINE']
		[@ww.label label='Script Body' name='com.trusolve.scriptengine.scriptbody' /]
	[/@ui.bambooSection]
[/@ui.bambooSection]