[#import "/lib/ace.ftl" as ace ]

[@ww.select
        label='Script location'
        listKey='key'
        listValue='value'
        list=locationTypes
        name='scriptlocation'
        toggle=true /]

[@ww.textfield name='scripttype' label='Script Type' required='true' description='The script language type.' /]

[@ui.bambooSection dependsOn='scriptlocation' showOn='FILE']
    [@ww.textfield label='Script File' name='script' required=true cssClass="long-field" /]
[/@ui.bambooSection]

[@ui.bambooSection dependsOn='scriptlocation' showOn='INLINE']
	[@ww.component template="ace-textarea.ftl" label='Script Body' name='scriptbody' required=true/]
[/@ui.bambooSection]