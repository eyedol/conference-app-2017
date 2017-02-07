package io.github.droidkaigi.confsched2017.viewmodel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.annotation.Nullable;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import io.github.droidkaigi.confsched2017.R;
import io.github.droidkaigi.confsched2017.model.Sponsorship;
import io.github.droidkaigi.confsched2017.util.AssetsUtil;
import io.reactivex.Single;

public final class SponsorshipsViewModel extends BaseObservable implements ViewModel {

    private Context context;

    @Inject
    SponsorshipsViewModel(Context context) {
        this.context = context;
    }

    @Override
    public void destroy() {
        // No-op
    }

    public Single<List<SponsorshipViewModel>> getSponsorships() {
        return loadSponsors().map(sponsorships ->
                Stream.of(sponsorships).map(SponsorshipViewModel::new).collect(Collectors.toList()));
    }


    private Single<List<Sponsorship>> loadSponsors() {
        return Single.create(emitter -> {
            try {
                final String json = AssetsUtil.loadJSONFromAsset(context, context.getString(R.string.sponsors_file));
                emitter.onSuccess(transformSponsorships(json));
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    /**
     * Transforms from a valid json string to a List of {@link Sponsorship}.
     *
     * @param json A json representing a list of sponsors.
     * @return List of {@link Sponsorship}.
     */
    @Nullable
    private List<Sponsorship> transformSponsorships(String json) {
        final Gson gson = new Gson();
        final Type listType = new TypeToken<List<Sponsorship>>() {
        }.getType();
        return gson.fromJson(json, listType);
    }
}
