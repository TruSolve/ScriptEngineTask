[#import "/lib/ace.ftl" as ace ]

[@ww.select
        labelKey='task.script.location'
        listKey='key'
        listValue='value'
        list=locationTypes
        name='scriptLocation'
        toggle=true /]

[@ww.textfield name='com.trusolve.scripthostTask.scripttype' label='Script Type' required='true' description='The script language type.' /]
[@ww.textarea name='com.trusolve.scripthostTask.scriptbody' label='Script body' cssClass="long-field" rows="10" required='true' description='The script text to execute.' /]
[@ui.bambooSection dependsOn='scriptLocation' showOn='FILE']
    [@ww.textfield labelKey='task.script.script' name='script' required=true cssClass="long-field" /]
[/@ui.bambooSection]

[@ui.bambooSection dependsOn='scriptLocation' showOn='INLINE']
    [@ace.textarea labelKey='task.script.body' name="scriptBody" required=true/]
[/@ui.bambooSection]