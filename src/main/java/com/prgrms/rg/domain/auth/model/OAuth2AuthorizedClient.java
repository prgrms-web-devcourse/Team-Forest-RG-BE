package com.prgrms.rg.domain.auth.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "oauth2_authorized_client")
public class OAuth2AuthorizedClient {

	@EmbeddedId
	private Key primaryKey;

	@Column(length = 100, nullable = false, name = "access_token_type")
	private String accessTokenType;

	@Lob
	@Column(name = "access_token_value", columnDefinition = "blob", nullable = false)
	private byte[] accessTokenValue;

	@Column(name = "access_token_issued_at", columnDefinition = "timestamp", nullable = false)
	private LocalDateTime accessTokenIssuedAt;

	@Column(name = "access_token_expires_at", columnDefinition = "timestamp", nullable = false)
	private LocalDateTime accessTokenExpiresAt;

	@Column(name = "access_token_scopes", length = 1000, nullable = false)
	private String accessTokenScopes;

	@Lob
	@Column(name = "refresh_token_value", columnDefinition = "blob")
	private byte[] refreshTokenValue;

	@Column(name = "refresh_token_issued_at", columnDefinition = "timestamp")
	private LocalDateTime refreshTokenIssuedAt;

	@Column(name = "created_at", columnDefinition = "timestamp default CURRENT_TIMESTAMP not null")
	private LocalDateTime createdAt;

	public Key getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Key primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Embeddable
	static class Key implements Serializable {
		@Column(name = "client_registration_id", length = 100, nullable = false)
		private String clientRegistrationId;
		@Column(name = "principal_name", length = 200, nullable = false)
		private String principalName;

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof Key))
				return false;
			Key key = (Key)o;
			return clientRegistrationId.equals(key.clientRegistrationId) && principalName.equals(key.principalName);
		}

		@Override
		public int hashCode() {
			return Objects.hash(clientRegistrationId, principalName);
		}
	}
}
