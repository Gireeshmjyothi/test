@Service
public class NotificationService {

    private final Map<String, Consumer<String>> notificationActions;

    public NotificationService() {
        this.notificationActions = Map.of(
            "smsMerchant", phone -> sendSms(phone, "Merchant SMS Notification"),
            "smsCustomer", phone -> sendSms(phone, "Customer SMS Notification"),
            "emailMerchant", email -> sendEmail(email, "Merchant Email Notification", "Your merchant email content here"),
            "emailCustomer", email -> sendEmail(email, "Customer Email Notification", "Your customer email content here")
        );
    }

    public void processNotification(NotificationDTO notificationDTO, String merchantEmail, String merchantPhone, String customerEmail, String customerPhone) {
        Map<String, String> recipients = Map.of(
            "smsMerchant", merchantPhone,
            "smsCustomer", customerPhone,
            "emailMerchant", merchantEmail,
            "emailCustomer", customerEmail
        );

        Map<String, String> flags = Map.of(
            "smsMerchant", notificationDTO.getSmsForMerchant(),
            "smsCustomer", notificationDTO.getSmsForCustomer(),
            "emailMerchant", notificationDTO.getEmailForMerchant(),
            "emailCustomer", notificationDTO.getEmailForCustomer()
        );

        // Use Streams to process notifications dynamically
        flags.forEach((key, value) -> {
            if ("Y".equalsIgnoreCase(value)) {
                notificationActions.get(key).accept(recipients.get(key));
            }
        });
    }

    private void sendSms(String phoneNumber, String message) {
        System.out.println("Sending SMS to " + phoneNumber + ": " + message);
    }

    private void sendEmail(String email, String subject, String body) {
        System.out.println("Sending Email to " + email + " - Subject: " + subject);
    }
}
