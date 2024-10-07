package com.gmail.marcosav2010.crm.user.usecases;

import com.gmail.marcosav2010.crm.shared.constants.PageConstants;
import com.gmail.marcosav2010.crm.shared.entities.Page;
import com.gmail.marcosav2010.crm.shared.entities.Paged;
import com.gmail.marcosav2010.crm.user.entities.User;
import com.gmail.marcosav2010.crm.user.entities.UserListRequest;
import com.gmail.marcosav2010.crm.user.ports.UserPort;
import java.util.List;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@CustomLog
@Component
@RequiredArgsConstructor
public class ListActiveUsersImpl implements ListActiveUsers {

  private final UserPort userPort;

  @Override
  public Paged<User> execute(UserListRequest request) {
    log.debug("Listing users for {}", request);

    if (request.page().size() > PageConstants.MAX_PAGE_SIZE) {
      throw new IllegalArgumentException(
          "Page size exceeds maximum allowed: " + PageConstants.MAX_PAGE_SIZE);
    }

    final Page requestedPage = request.page();
    final List<User> users = userPort.findActive(requestedPage.page(), requestedPage.size());

    final long results = userPort.countActive();

    return Paged.<User>builder().data(users).results(results).build();
  }
}
