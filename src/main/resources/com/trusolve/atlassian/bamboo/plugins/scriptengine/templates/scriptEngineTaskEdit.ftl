[@ww.select
        label='Script location'
        listKey='key'
        listValue='value'
        list=locationTypes
        name='com.trusolve.scriptengine.scriptlocation'
        toggle=true /]

[@ww.textfield name='com.trusolve.scriptengine.scripttype' label='Script Type' required='true' description='The script language type.' /]

[@ui.bambooSection dependsOn='com.trusolve.scriptengine.scriptlocation' showOn='FILE']
    [@ww.textfield label='Script File' name='com.trusolve.scriptengine.scriptfile' required=true cssClass="long-field" /]
[/@ui.bambooSection]

[@ui.bambooSection dependsOn='com.trusolve.scriptengine.scriptlocation' showOn='INLINE']
	[@ww.component template="ace-textarea.ftl" label='Script Body' name='com.trusolve.scriptengine.scriptbody' required=true/]
[/@ui.bambooSection]