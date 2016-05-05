package com.pubnub.api.endpoints.push;

import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.builder.PubNubErrorBuilder;
import com.pubnub.api.endpoints.Endpoint;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.push.PNPushRemoveAllChannelsResult;
import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.Map;

@Accessors(chain = true, fluent = true)
public class RemoveAllPushChannelsForDevice extends Endpoint<List<Object>, PNPushRemoveAllChannelsResult> {

    @Setter private PNPushType pushType;
    @Setter private String deviceId;

    public RemoveAllPushChannelsForDevice(PubNub pubnub) {
        super(pubnub);
    }

    @Override
    protected void validateParams() throws PubNubException {
        if (pubnub.getConfiguration().getSubscribeKey()==null || pubnub.getConfiguration().getSubscribeKey().isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_SUBSCRIBE_KEY_MISSING).build();
        }
        if (pushType == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PUSH_TYPE_MISSING).build();
        }
        if (deviceId == null || deviceId.isEmpty()) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_DEVICE_ID_MISSING).build();
        }
    }


    @Override
    protected Call<List<Object>> doWork(Map<String, String> params) throws PubNubException {
        params.put("type", pushType.name().toLowerCase());

        PushService service = this.createRetrofit().create(PushService.class);

        return service.removeAllChannelsForDevice(pubnub.getConfiguration().getSubscribeKey(), deviceId, params);

    }

    @Override
    protected PNPushRemoveAllChannelsResult createResponse(Response<List<Object>> input) throws PubNubException {
        if (input.body() == null) {
            throw PubNubException.builder().pubnubError(PubNubErrorBuilder.PNERROBJ_PARSING_ERROR).build();
        }

        return PNPushRemoveAllChannelsResult.builder().build();
    }

    protected int getConnectTimeout() {
        return pubnub.getConfiguration().getConnectTimeout();
    }

    protected int getRequestTimeout() {
        return pubnub.getConfiguration().getNonSubscribeRequestTimeout();
    }

    @Override
    protected PNOperationType getOperationType() {
        return null; // PNOperationType.PNPushNotificationModifiedChannelsOperations;
    }

}
