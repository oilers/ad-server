package org.oiler.ad.server.resources

import io.dropwizard.hibernate.UnitOfWork
import org.oiler.ad.server.api.UserModel
import org.oiler.ad.server.core.AdSizeService
import org.oiler.ad.server.core.ProviderService
import org.oiler.ad.server.core.UserService
import org.oiler.ad.server.entities.User

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by Kodi on 2/4/2017.
 */
@Path("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class UserResource {
    UserService userService
    AdSizeService adSizeService
    ProviderService providerService

    @GET
    @Path("{userId}")
    @UnitOfWork
    public UserModel getUser(@PathParam("userId") int userId) {
        return userService.getUser(userId).toModel()
    }

    @GET
    @UnitOfWork
    public Collection<UserModel> getUsers() {
        return userService.users.collect { it.toModel() }
    }

    @POST
    @Path("{userId}")
    @UnitOfWork
    public UserModel updateUser(@PathParam("userId") int userId, User user) {
        if (userId == user.userId) {
            return userService.saveUser(user).toModel()
        } else {
            throw new WebApplicationException(
                    Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                            .entity([errorMsg: "userIds do not match"]).build())
        }
    }

    @POST
    @Path("{userId}/adSize/{adSizeId}")
    @UnitOfWork
    public UserModel associateAdSize(@PathParam("userId") int userId, @PathParam("adSizeId") int adSizeId) {
        def user = userService.getUser(userId)
        def adSize = adSizeService.getAdSize(adSizeId)
        return userService.associateAdSize(user, adSize).toModel()
    }

    @POST
    @Path("{userId}/provider/{providerId}")
    @UnitOfWork
    public UserModel associateProvider(@PathParam("userId") int userId, @PathParam("providerId") int providerId) {
        def user = userService.getUser(userId)
        def provider = providerService.getProvider(providerId)
        return userService.associateProvider(user, provider).toModel()
    }

    @PUT
    @UnitOfWork
    public UserModel createUser(User user) {
        return userService.saveUser(user).toModel()
    }

}
