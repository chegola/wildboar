package com.wildboar.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Wildboar.
 * <p>
 * Properties are configured in the application.yml file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
	
	private Line line = new Line();

	public Line getLine() {
		return this.line;
	}

	public static class Line {
		private String successURL;
		private String failedURL;
		private String lineBotChannelSecret;
		private String lineBotChannelToken;

		public String getLineBotChannelSecret() {
			return System.getenv(lineBotChannelSecret);
		}

		public void setLineBotChannelSecret(String lineBotChannelSecret) {
			this.lineBotChannelSecret = lineBotChannelSecret;
		}

		public String getLineBotChannelToken() {
			return System.getenv(lineBotChannelToken);
		}

		public void setLineBotChannelToken(String lineBotChannelToken) {
			this.lineBotChannelToken = lineBotChannelToken;
		}


		public String getSuccessURL() {
			return successURL;
		}

		public void setSuccessURL(String successURL) {
			this.successURL = successURL;
		}

		public String getFailedURL() {
			return failedURL;
		}

		public void setFailedURL(String failedURL) {
			this.failedURL = failedURL;
		}

		@Override
		public String toString() {
			return "LINE{" + "SuccessURL=" + this.getSuccessURL() + " " + "FailedURL=" + this.getFailedURL() + " "
					+ "LineBotChannelSecret=" + this.getLineBotChannelSecret() + " " + "LineBotChannelToken="
					+ this.getLineBotChannelToken() + " " + '}';
		}

	}


}
