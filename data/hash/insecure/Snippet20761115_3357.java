def calculateHashedMessages() {
log.info('CalculatedHashedMessages')
def hashedMessages = [:]
def digestCalculator = MessageDigest.getInstance("SHA-1")

db.eachRow("""
            select top 1000 phone, text, id
            from proxyTable
            where dlr_description is null
            and processed_at is null
            and hashed is null
            """) { row ->
  hashedMessages[row['id']] = calculateHashForRow(row, digestCalculator)
}

hashedMessages
}
