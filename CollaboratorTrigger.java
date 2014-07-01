trigger Project_Share on Tasky_Collaborator__c (after insert, after delete) {

    if (trigger.isInsert) {
        List<Tasky_Project__Share> projectShares = new List<Tasky_Project__Share>();
        for (Tasky_Collaborator__c collaborator: trigger.new) {
            Tasky_Project__Share collaboratorShare = new Tasky_Project__Share();
            collaboratorShare.ParentId = collaborator.Project__c;
            collaboratorShare.UserOrGroupId = collaborator.User__c;
            collaboratorShare.AccessLevel = 'All';
            collaboratorShare.RowCause = Schema.Tasky_Project__Share.RowCause.Collaborator_Access__c;
            projectShares.add(collaboratorShare);
        }
        Database.SaveResult[] projectShareInsertResult = Database.insert(projectShares,false);
    }

}