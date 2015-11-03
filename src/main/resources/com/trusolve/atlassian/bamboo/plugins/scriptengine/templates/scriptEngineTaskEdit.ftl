[@ww.select
        label='Script location'
        listKey='key'
        listValue='value'
        list='com_trusolve_scriptengine_locationTypes'
        name='com_trusolve_scriptengine_scriptlocation'
        toggle=true /]

[@ui.bambooSection dependsOn='com_trusolve_scriptengine_isDeployment' showOn='true']
    [@ww.checkbox label='Run On Server' name='com_trusolve_scriptengine_deployRunOnServer' /]
[/@ui.bambooSection]

[@ww.textfield name='com_trusolve_scriptengine_scripttype' label='Script Type' required='true' description='The script language type.' /]

[@ui.bambooSection dependsOn='com_trusolve_scriptengine_scriptlocation' showOn='FILE']
    [@ww.textfield label='Script File' name='com_trusolve_scriptengine_scriptfile' required=true cssClass="long-field" /]
[/@ui.bambooSection]

[@ui.bambooSection dependsOn='com_trusolve_scriptengine_scriptlocation' showOn='INLINE']
	[@ww.component template="ace-textarea.ftl" label='Script Body' name='com_trusolve_scriptengine_scriptbody' required=true/]
[/@ui.bambooSection]