trigger Project_Share on Tasky_Collaborator__c (after insert, before delete) {

    if (trigger.isInsert) {
        List<Tasky_Project__Share> projectShares = new List<Tasky_Project__Share>();
        for (Tasky_Collaborator__c collaborator: trigger.new) {
            Tasky_Project__Share collaboratorShare = new Tasky_Project__Share();
            collaboratorShare.ParentId = collaborator.Project__c;
            collaboratorShare.UserOrGroupId = collaborator.User__c;
            collaboratorShare.AccessLevel = 'edit';
            collaboratorShare.RowCause = Schema.Tasky_Project__Share.RowCause.Collaborator_Access__c;
            projectShares.add(collaboratorShare);
        }
        Database.SaveResult[] projectShareInsertResult = Database.insert(projectShares,false);
    } else if (trigger.isDelete) {
        List<Tasky_Project__Share> projectShares = new List<Tasky_Project__Share>();

        for (Tasky_Collaborator__c collaborator: trigger.old) {
            Tasky_Project__Share[] collaboratorShares = [SELECT Id FROM Tasky_Project__Share WHERE ParentId =: collaborator.Project__c AND UserOrGroupId =: collaborator.User__c];
            projectShares.add(collaboratorShares[0]);
        }
        Database.DeleteResult[] projectShareInsertResult = Database.delete(projectShares,false);
    }
}