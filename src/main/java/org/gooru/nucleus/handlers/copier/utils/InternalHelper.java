package org.gooru.nucleus.handlers.copier.utils;

import java.util.UUID;

import org.gooru.nucleus.handlers.copier.constants.MessageConstants;

public final class InternalHelper {

  public static boolean validateUser(String userId) {
    return !(userId == null || userId.isEmpty()) && (userId.equalsIgnoreCase(MessageConstants.MSG_USER_ANONYMOUS) || validateUuid(userId));
  }

  public static boolean validateId(String id) {
    return !(id == null || id.isEmpty()) && validateUuid(id);
  }

  private static boolean validateUuid(String uuidString) {
    try {
      UUID.fromString(uuidString);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    } catch (Exception e) {
      return false;
    }
  }
}
