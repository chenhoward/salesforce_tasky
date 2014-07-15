trigger Collaborator_Create on Tasky_Project__c (after insert) {
    if (trigger.isInsert) {
        for (Tasky_Project__c proj: trigger.new){
            Tasky_Collaborator__c collaborator = new Tasky_Collaborator__c();
            collaborator.Project__c = proj.Id;
            collaborator.User__c = UserInfo.getUserId();
            collaborator.Name = UserInfo.getName();
            insert collaborator;
        }
    }
}

trigger Task_Complete on Tasky_Task__c (before insert, before update) {
    if (trigger.isInsert || trigger.isUpdate) {
        for (Tasky_Task__c task: trigger.new){
            if (task.Status__c == 'Done'){
                task.Completed_Date__c = System.now();
            }
        }
    }
}