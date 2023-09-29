package step.learning.services.email;

public interface EmailService {
    /**
     * створення шаблону повідомлення
     * @return Повідомлення із заповненими Subject From тощо
     */
    javax.mail.Message prepareMessage(); // prepareMessage(String sample)- для різних шаблонів
                                        // (з днем народження, акційна пропозиція, підтвердження коду, тощо)

    /**
     * Надсилання "заповненого" повыдомлення
     * @param message - доповнений шаблон повідомлення
     */
    void send(javax.mail.Message message);
}
