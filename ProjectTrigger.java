trigger Collaborator_Create on Tasky_Project__c (after insert) {
    Tasky_Collaborator__c[] collaborators = new List<Tasky_Collaborator__c>();
    for (Tasky_Project__c proj: trigger.new){
        Tasky_Collaborator__c collaborator = new Tasky_Collaborator__c();
        collaborator.Project__c = proj.Id;
        collaborator.User__c = UserInfo.getUserId();
        collaborator.Name = UserInfo.getName();
        collaborators.add(collaborator);
    }
    insert collaborators;

}

trigger Task_Complete on Tasky_Task__c (before insert, before update) {
    for (Tasky_Task__c task: trigger.new){
        if (task.Status__c == 'Done'){
            task.Completed_Date__c = System.today();
        }
    }
}

trigger Task_Assign on Tasky_Task__c (before insert, before update) {
    Id[] assigneeIds = new List<Id>();
    Id[] projectIds = new List<Id>();
    for (Tasky_Task__c task: trigger.new) {
        assigneeIds.add(task.Assignee__c);
        projectIds.add(task.Project__c);
    }
    Tasky_Collaborator__c[] collaborators = [SELECT Project__c, Id, User__c FROM Tasky_Collaborator__c WHERE Project__c IN :projectIds AND User__c IN :assigneeIds];
    for (Tasky_Task__c task:trigger.new) {
        Boolean hasCollaborator = false;
        for (Tasky_Collaborator__c collaborator: collaborators) {
            if (task.Assignee__c == collaborator.User__c) {
                hasCollaborator = true;
            }
        }
        if (!hasCollaborator && task.Assignee__c != null) {
            task.addError('User is not a collaborator in this project');
        }
    }
}