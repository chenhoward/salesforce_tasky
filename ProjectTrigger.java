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