package com.erwintobing15.disposisi.network;

import com.erwintobing15.disposisi.config.Constants;
import com.erwintobing15.disposisi.model.agendamou.AgendaMouModel;
import com.erwintobing15.disposisi.model.bast.BastModel;
import com.erwintobing15.disposisi.model.message.MessageModel;
import com.erwintobing15.disposisi.model.nodin.NodinModel;
import com.erwintobing15.disposisi.model.perjanjiankerjasama.PerjanjianKerjasamaModel;
import com.erwintobing15.disposisi.model.referensi.ReferensiModel;
import com.erwintobing15.disposisi.model.agendamou.SelectAgendaMouModel;
import com.erwintobing15.disposisi.model.bast.SelectBastModel;
import com.erwintobing15.disposisi.model.nodin.SelectNodinModel;
import com.erwintobing15.disposisi.model.perjanjiankerjasama.SelectPerjanjianKerjasamaModel;
import com.erwintobing15.disposisi.model.sptjm.SelectSptjmModel;
import com.erwintobing15.disposisi.model.suratkeputusan.SelectSuratKeputusanModel;
import com.erwintobing15.disposisi.model.suratketerangan.SelectSuratKeteranganModel;
import com.erwintobing15.disposisi.model.suratperintah.SelectSuratPerintahModel;
import com.erwintobing15.disposisi.model.sptjm.SptjmModel;
import com.erwintobing15.disposisi.model.suratkeputusan.SuratKeputusanModel;
import com.erwintobing15.disposisi.model.suratketerangan.SuratKeteranganModel;
import com.erwintobing15.disposisi.model.suratpengantarkeluar.SuratPengantarKeluarModel;
import com.erwintobing15.disposisi.model.suratmasukundangan.SuratMasukUndanganModel;
import com.erwintobing15.disposisi.model.referensi.SelectReferensiModel;
import com.erwintobing15.disposisi.model.suratpengantarkeluar.SelectSuratPengantarKeluarModel;
import com.erwintobing15.disposisi.model.suratmasukundangan.SelectSuratMasukUndanganModel;
import com.erwintobing15.disposisi.model.suratkeluar.SelectSuratKeluarModel;
import com.erwintobing15.disposisi.model.suratmasuk.SelectSuratMasukModel;
import com.erwintobing15.disposisi.model.suratkeluar.SuratKeluarModel;
import com.erwintobing15.disposisi.model.suratmasuk.SuratMasukModel;
import com.erwintobing15.disposisi.model.suratperintah.SuratPerintahModel;
import com.erwintobing15.disposisi.model.user.UserModel;

import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIService {

    /**
     * Fetch user API endpoint
     *
     * @param username
     * @param password
     * @return
     */

    @FormUrlEncoded
    @POST("api/login.php")
    Call<UserModel.UserDataModel> postLogin(@Field("username") String username,
                                            @Field("password") String password);

    @FormUrlEncoded
    @POST("api/allUser.php")
    Call<UserModel.UserDataModel> allUser(@Field("id") String id);

    /**
     * Fetch surat masuk API endpoint
     *
     * @param id
     * @return
     */

    @FormUrlEncoded
    @POST("api/allSuratMasuk.php")
    Call<SuratMasukModel.SuratMasukDataModel> allSuratMasuk(@Field("id") String id);

    @FormUrlEncoded
    @POST("api/oneSuratMasuk.php")
    Call<SelectSuratMasukModel> oneSuratMasuk(@Field("id") String id);

    @Multipart
    @POST("api/insertSuratMasuk.php")
    Call<MessageModel> postInsertSuratMasuk(@Part("no_agenda") RequestBody noAgenda,
                                            @Part("no_surat") RequestBody noSurat,
                                            @Part("asal_surat") RequestBody asalSurat,
                                            @Part("isi") RequestBody isi,
                                            @Part("kode") RequestBody kode,
                                            @Part("indeks") RequestBody indeks,
                                            @Part("tgl_surat") RequestBody tanggalSurat,
                                            @Part("keterangan") RequestBody keteranganan,
                                            @Part("id_user") RequestBody idUser,
                                            @Part MultipartBody.Part file);

    @Multipart
    @POST("api/updateSuratMasuk.php")
    Call<MessageModel> postUpdateSuratMasuk(@Part("id") RequestBody id,
                                            @Part("no_agenda") RequestBody noAgenda,
                                            @Part("no_surat") RequestBody noSurat,
                                            @Part("asal_surat") RequestBody asalSurat,
                                            @Part("isi") RequestBody isi,
                                            @Part("kode") RequestBody kode,
                                            @Part("indeks") RequestBody indeks,
                                            @Part("tgl_surat") RequestBody tanggalSurat,
                                            @Part("keterangan") RequestBody keteranganan,
                                            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/deleteSuratMasuk.php")
    Call<MessageModel> postDeleteSuratMasuk(@Field("id") String id);

    /**
     * Fetch surat keluar API endpoint
     *
     * @param id
     * @return
     */

    @FormUrlEncoded
    @POST("api/transaksi/suratkeluar/allSuratKeluar.php")
    Call<SuratKeluarModel.SuratKeluarDataModel> allSuratKeluar(@Field("id") String id);

    @FormUrlEncoded
    @POST("api/transaksi/suratkeluar/deleteSuratKeluar.php")
    Call<MessageModel> postDeleteSuratKeluar(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/suratkeluar/insertSuratKeluar.php")
    Call<MessageModel> postInsertSuratKeluar(@Part("no_agenda") RequestBody noAgenda,
                                            @Part("no_surat") RequestBody noSurat,
                                            @Part("tujuan") RequestBody tujuan,
                                            @Part("isi") RequestBody isi,
                                            @Part("tgl_surat") RequestBody tanggalSurat,
                                            @Part("keterangan") RequestBody keteranganan,
                                            @Part("id_user") RequestBody idUser,
                                            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/transaksi/suratkeluar/oneSuratKeluar.php")
    Call<SelectSuratKeluarModel> oneSuratKeluar(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/suratkeluar/updateSuratKeluar.php")
    Call<MessageModel> postUpdateSuratKeluar(@Part("id") RequestBody id,
                                             @Part("no_agenda") RequestBody noAgenda,
                                             @Part("no_surat") RequestBody noSurat,
                                             @Part("tujuan") RequestBody tujuan,
                                             @Part("isi") RequestBody isi,
                                             @Part("tgl_surat") RequestBody tanggalSurat,
                                             @Part("keterangan") RequestBody keteranganan,
                                             @Part MultipartBody.Part file);

    /**
     * Fetch surat pengantar keluar API endpoint
     *
     */

    @FormUrlEncoded
    @POST("api/transaksi/suratpengantarkeluar/allSuratPengantarKeluar.php")
    Call<SuratPengantarKeluarModel.SuratPengantarKeluarDataModel> allSuratPengantarKeluar(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/suratpengantarkeluar/insertSuratPengantarKeluar.php")
    Call<MessageModel> postInsertSuratPengantarKeluar(@Part("no_agenda") RequestBody noAgenda,
                                             @Part("no_surat") RequestBody noSurat,
                                             @Part("tujuan") RequestBody tujuan,
                                             @Part("isi") RequestBody isi,
                                             @Part("tgl_surat") RequestBody tanggalSurat,
                                             @Part("keterangan") RequestBody keteranganan,
                                             @Part("id_user") RequestBody idUser,
                                             @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/transaksi/suratpengantarkeluar/deleteSuratPengantarKeluar.php")
    Call<MessageModel> postDeleteSuratPengantarKeluar(@Field("id") String id);

    @FormUrlEncoded
    @POST("api/transaksi/suratpengantarkeluar/oneSuratPengantarKeluar.php")
    Call<SelectSuratPengantarKeluarModel> oneSuratPengantarKeluar(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/suratpengantarkeluar/updateSuratPengantarKeluar.php")
    Call<MessageModel> postUpdateSuratPengantarKeluar(@Part("id") RequestBody id,
                                             @Part("no_agenda") RequestBody noAgenda,
                                             @Part("no_surat") RequestBody noSurat,
                                             @Part("tujuan") RequestBody tujuan,
                                             @Part("isi") RequestBody isi,
                                             @Part("tgl_surat") RequestBody tanggalSurat,
                                             @Part("keterangan") RequestBody keteranganan,
                                             @Part MultipartBody.Part file);

    /**
     * Fetch surat masuk undangan API endpoint
     *
     */

    @FormUrlEncoded
    @POST("api/transaksi/suratmasukundangan/allSuratMasukUndangan.php")
    Call<SuratMasukUndanganModel.SuratMasukUndanganDataModel> allSuratMasukUndangan(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/suratmasukundangan/insertSuratMasukUndangan.php")
    Call<MessageModel> postInsertSuratMasukUndangan(@Part("no_agenda") RequestBody noAgenda,
                                          @Part("no_surat") RequestBody noSurat,
                                          @Part("asal_surat") RequestBody asalSurat,
                                          @Part("isi") RequestBody isi,
                                          @Part("kode") RequestBody kode,
                                          @Part("tgl_surat") RequestBody tanggalSurat,
                                          @Part("keterangan") RequestBody keteranganan,
                                          @Part("id_user") RequestBody idUser,
                                          @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/transaksi/suratmasukundangan/deleteSuratMasukUndangan.php")
    Call<MessageModel> postdeleteSuratMasukUndangan(@Field("id") String id);

    @FormUrlEncoded
    @POST("api/transaksi/suratmasukundangan/oneSuratMasukUndangan.php")
    Call<SelectSuratMasukUndanganModel> oneSuratMasukUndangan(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/suratmasukundangan/updateSuratMasukUndangan.php")
    Call<MessageModel> updateSuratMasukUndangan(@Part("id") RequestBody id,
                                     @Part("no_agenda") RequestBody noAgenda,
                                     @Part("no_surat") RequestBody noSurat,
                                     @Part("asal_surat") RequestBody asalSurat,
                                     @Part("isi") RequestBody isi,
                                     @Part("kode") RequestBody kode,
                                     @Part("tgl_surat") RequestBody tanggalSurat,
                                     @Part("keterangan") RequestBody keteranganan,
                                     @Part MultipartBody.Part file);

    /**
     * Fetch surat perintah API endpoint
     *
     */

    @FormUrlEncoded
    @POST("api/transaksi/suratperintah/allSuratPerintah.php")
    Call<SuratPerintahModel.SuratPerintahDataModel> allSuratPerintah(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/suratperintah/insertSuratPerintah.php")
    Call<MessageModel> postInsertSuratPerintah(@Part("no_agenda") RequestBody noAgenda,
                                         @Part("no_surat") RequestBody noSurat,
                                         @Part("tujuan") RequestBody asalSurat,
                                         @Part("isi") RequestBody isi,
                                         @Part("tgl_surat") RequestBody tanggalSurat,
                                         @Part("keterangan") RequestBody keteranganan,
                                         @Part("id_user") RequestBody idUser,
                                         @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/transaksi/suratperintah/deleteSuratPerintah.php")
    Call<MessageModel> postdeleteSuratPerintah(@Field("id") String id);

    @FormUrlEncoded
    @POST("api/transaksi/suratperintah/oneSuratPerintah.php")
    Call<SelectSuratPerintahModel> oneSuratPerintah(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/suratperintah/updateSuratPerintah.php")
    Call<MessageModel> updateSuratPerintah(@Part("id") RequestBody id,
                                     @Part("no_agenda") RequestBody noAgenda,
                                     @Part("no_surat") RequestBody noSurat,
                                     @Part("tujuan") RequestBody asalSurat,
                                     @Part("isi") RequestBody isi,
                                     @Part("tgl_surat") RequestBody tanggalSurat,
                                     @Part("keterangan") RequestBody keteranganan,
                                     @Part MultipartBody.Part file);

    /**
     * Fetch surat keputusan API endpoint
     *
     */

    @FormUrlEncoded
    @POST("api/transaksi/suratkeputusan/allSuratKeputusan.php")
    Call<SuratKeputusanModel.SuratKeputusanDataModel> allSuratKeputusan(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/suratkeputusan/insertSuratKeputusan.php")
    Call<MessageModel> postInsertSuratKeputusan(@Part("no_agenda") RequestBody noAgenda,
                                               @Part("no_surat") RequestBody noSurat,
                                               @Part("tujuan") RequestBody asalSurat,
                                               @Part("isi") RequestBody isi,
                                               @Part("tgl_surat") RequestBody tanggalSurat,
                                               @Part("keterangan") RequestBody keteranganan,
                                               @Part("id_user") RequestBody idUser,
                                               @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/transaksi/suratkeputusan/deleteSuratKeputusan.php")
    Call<MessageModel> postdeleteSuratKeputusan(@Field("id") String id);

    @FormUrlEncoded
    @POST("api/transaksi/suratkeputusan/oneSuratKeputusan.php")
    Call<SelectSuratKeputusanModel> oneSuratKeputusan(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/suratkeputusan/updateSuratKeputusan.php")
    Call<MessageModel> updateSuratKeputusan(@Part("id") RequestBody id,
                                           @Part("no_agenda") RequestBody noAgenda,
                                           @Part("no_surat") RequestBody noSurat,
                                           @Part("tujuan") RequestBody asalSurat,
                                           @Part("isi") RequestBody isi,
                                           @Part("tgl_surat") RequestBody tanggalSurat,
                                           @Part("keterangan") RequestBody keteranganan,
                                           @Part MultipartBody.Part file);

    /**
     * Fetch Nodin API endpoint
     *
     */

    @FormUrlEncoded
    @POST("api/transaksi/nodin/allNodin.php")
    Call<NodinModel.NodinDataModel> allNodin(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/nodin/insertNodin.php")
    Call<MessageModel> postInsertNodin(@Part("no_agenda") RequestBody noAgenda,
                                                @Part("no_surat") RequestBody noSurat,
                                                @Part("tujuan") RequestBody asalSurat,
                                                @Part("isi") RequestBody isi,
                                                @Part("tgl_surat") RequestBody tanggalSurat,
                                                @Part("keterangan") RequestBody keteranganan,
                                                @Part("id_user") RequestBody idUser,
                                                @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/transaksi/nodin/deleteNodin.php")
    Call<MessageModel> postdeleteNodin(@Field("id") String id);

    @FormUrlEncoded
    @POST("api/transaksi/nodin/oneNodin.php")
    Call<SelectNodinModel> oneNodin(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/nodin/updateNodin.php")
    Call<MessageModel> updateNodin(@Part("id") RequestBody id,
                                            @Part("no_agenda") RequestBody noAgenda,
                                            @Part("no_surat") RequestBody noSurat,
                                            @Part("tujuan") RequestBody asalSurat,
                                            @Part("isi") RequestBody isi,
                                            @Part("tgl_surat") RequestBody tanggalSurat,
                                            @Part("keterangan") RequestBody keteranganan,
                                            @Part MultipartBody.Part file);

    /**
     * Fetch Perjanjian Kerjasama API endpoint
     *
     */

    @FormUrlEncoded
    @POST("api/transaksi/perjanjiankerjasama/allPerjanjianKerjasama.php")
    Call<PerjanjianKerjasamaModel.PerjanjianKerjasamaDataModel> allPerjanjianKerjasama(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/perjanjiankerjasama/insertPerjanjianKerjasama.php")
    Call<MessageModel> postInsertPerjanjianKerjasama(@Part("no_agenda") RequestBody noAgenda,
                                       @Part("no_surat") RequestBody noSurat,
                                       @Part("tujuan") RequestBody asalSurat,
                                       @Part("isi") RequestBody isi,
                                       @Part("tgl_surat") RequestBody tanggalSurat,
                                       @Part("keterangan") RequestBody keteranganan,
                                       @Part("id_user") RequestBody idUser,
                                       @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/transaksi/perjanjiankerjasama/deletePerjanjianKerjasama.php")
    Call<MessageModel> postdeletePerjanjianKerjasama(@Field("id") String id);

    @FormUrlEncoded
    @POST("api/transaksi/perjanjiankerjasama/onePerjanjianKerjasama.php")
    Call<SelectPerjanjianKerjasamaModel> onePerjanjianKerjasama(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/perjanjiankerjasama/updatePerjanjianKerjasama.php")
    Call<MessageModel> updatePerjanjianKerjasama(@Part("id") RequestBody id,
                                   @Part("no_agenda") RequestBody noAgenda,
                                   @Part("no_surat") RequestBody noSurat,
                                   @Part("tujuan") RequestBody asalSurat,
                                   @Part("isi") RequestBody isi,
                                   @Part("tgl_surat") RequestBody tanggalSurat,
                                   @Part("keterangan") RequestBody keteranganan,
                                   @Part MultipartBody.Part file);

    /**
     * Fetch Agenda MOU API endpoint
     *
     */

    @FormUrlEncoded
    @POST("api/transaksi/agendamou/allAgendaMou.php")
    Call<AgendaMouModel.AgendaMouDataModel> allAgendaMou(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/agendamou/insertAgendaMou.php")
    Call<MessageModel> postInsertAgendaMou(@Part("no_agenda") RequestBody noAgenda,
                                                     @Part("no_surat") RequestBody noSurat,
                                                     @Part("tujuan") RequestBody asalSurat,
                                                     @Part("isi") RequestBody isi,
                                                     @Part("tgl_surat") RequestBody tanggalSurat,
                                                     @Part("keterangan") RequestBody keteranganan,
                                                     @Part("id_user") RequestBody idUser,
                                                     @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/transaksi/agendamou/deleteAgendaMou.php")
    Call<MessageModel> postdeleteAgendaMou(@Field("id") String id);

    @FormUrlEncoded
    @POST("api/transaksi/agendamou/oneAgendaMou.php")
    Call<SelectAgendaMouModel> oneAgendaMou(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/agendamou/updateAgendaMou.php")
    Call<MessageModel> updateAgendaMou(@Part("id") RequestBody id,
                                                 @Part("no_agenda") RequestBody noAgenda,
                                                 @Part("no_surat") RequestBody noSurat,
                                                 @Part("tujuan") RequestBody asalSurat,
                                                 @Part("isi") RequestBody isi,
                                                 @Part("tgl_surat") RequestBody tanggalSurat,
                                                 @Part("keterangan") RequestBody keteranganan,
                                                 @Part MultipartBody.Part file);

    /**
     * Fetch SPTJM API endpoint
     *
     */

    @FormUrlEncoded
    @POST("api/transaksi/sptjm/allSptjm.php")
    Call<SptjmModel.SptjmDataModel> allSptjm(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/sptjm/insertSptjm.php")
    Call<MessageModel> postInsertSptjm(@Part("no_agenda") RequestBody noAgenda,
                                           @Part("no_surat") RequestBody noSurat,
                                           @Part("tujuan") RequestBody asalSurat,
                                           @Part("isi") RequestBody isi,
                                           @Part("tgl_surat") RequestBody tanggalSurat,
                                           @Part("keterangan") RequestBody keteranganan,
                                           @Part("id_user") RequestBody idUser,
                                           @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/transaksi/sptjm/deleteSptjm.php")
    Call<MessageModel> postdeleteSptjm(@Field("id") String id);

    @FormUrlEncoded
    @POST("api/transaksi/sptjm/oneSptjm.php")
    Call<SelectSptjmModel> oneSptjm(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/sptjm/updateSptjm.php")
    Call<MessageModel> updateSptjm(@Part("id") RequestBody id,
                                       @Part("no_agenda") RequestBody noAgenda,
                                       @Part("no_surat") RequestBody noSurat,
                                       @Part("tujuan") RequestBody asalSurat,
                                       @Part("isi") RequestBody isi,
                                       @Part("tgl_surat") RequestBody tanggalSurat,
                                       @Part("keterangan") RequestBody keteranganan,
                                       @Part MultipartBody.Part file);

    /**
     * Fetch BAST API endpoint
     *
     */

    @FormUrlEncoded
    @POST("api/transaksi/bast/allBast.php")
    Call<BastModel.BastDataModel> allBast(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/bast/insertBast.php")
    Call<MessageModel> postInsertBast(@Part("no_agenda") RequestBody noAgenda,
                                       @Part("no_surat") RequestBody noSurat,
                                       @Part("tujuan") RequestBody asalSurat,
                                       @Part("isi") RequestBody isi,
                                       @Part("tgl_surat") RequestBody tanggalSurat,
                                       @Part("keterangan") RequestBody keteranganan,
                                       @Part("id_user") RequestBody idUser,
                                       @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/transaksi/bast/deleteBast.php")
    Call<MessageModel> postdeleteBast(@Field("id") String id);

    @FormUrlEncoded
    @POST("api/transaksi/bast/oneBast.php")
    Call<SelectBastModel> oneBast(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/bast/updateBast.php")
    Call<MessageModel> updateBast(@Part("id") RequestBody id,
                                   @Part("no_agenda") RequestBody noAgenda,
                                   @Part("no_surat") RequestBody noSurat,
                                   @Part("tujuan") RequestBody asalSurat,
                                   @Part("isi") RequestBody isi,
                                   @Part("tgl_surat") RequestBody tanggalSurat,
                                   @Part("keterangan") RequestBody keteranganan,
                                   @Part MultipartBody.Part file);

    /**
     * Fetch Surat Keterangan API endpoint
     *
     */

    @FormUrlEncoded
    @POST("api/transaksi/suratketerangan/allSuratKeterangan.php")
    Call<SuratKeteranganModel.SuratKeteranganDataModel> allSuratKeterangan(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/suratketerangan/insertSuratKeterangan.php")
    Call<MessageModel> postInsertSuratKeterangan(@Part("no_agenda") RequestBody noAgenda,
                                      @Part("no_surat") RequestBody noSurat,
                                      @Part("tujuan") RequestBody asalSurat,
                                      @Part("isi") RequestBody isi,
                                      @Part("tgl_surat") RequestBody tanggalSurat,
                                      @Part("keterangan") RequestBody keteranganan,
                                      @Part("id_user") RequestBody idUser,
                                      @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("api/transaksi/suratketerangan/deleteSuratKeterangan.php")
    Call<MessageModel> postdeleteSuratKeterangan(@Field("id") String id);

    @FormUrlEncoded
    @POST("api/transaksi/suratketerangan/oneSuratKeterangan.php")
    Call<SelectSuratKeteranganModel> oneSuratKeterangan(@Field("id") String id);

    @Multipart
    @POST("api/transaksi/suratketerangan/updateSuratKeterangan.php")
    Call<MessageModel> updateSuratKeterangan(@Part("id") RequestBody id,
                                  @Part("no_agenda") RequestBody noAgenda,
                                  @Part("no_surat") RequestBody noSurat,
                                  @Part("tujuan") RequestBody asalSurat,
                                  @Part("isi") RequestBody isi,
                                  @Part("tgl_surat") RequestBody tanggalSurat,
                                  @Part("keterangan") RequestBody keteranganan,
                                  @Part MultipartBody.Part file);

    /**
     * Fetch Referensi API endpoint
     *
     */

    @FormUrlEncoded
    @POST("api/referensi/allReferensi.php")
    Call<ReferensiModel.ReferensiDataModel> allReferensi(@Field("id") String id);

    @Multipart
    @POST("api/referensi/insertReferensi.php")
    Call<MessageModel> postInsertReferensi(@Part("kode") RequestBody kode,
                                           @Part("nama") RequestBody nama,
                                           @Part("uraian") RequestBody uraian,
                                           @Part("id_user") RequestBody idUser);

    @FormUrlEncoded
    @POST("api/referensi/deleteReferensi.php")
    Call<MessageModel> postdeleteReferensi(@Field("id") String id);

    @FormUrlEncoded
    @POST("api/referensi/oneReferensi.php")
    Call<SelectReferensiModel> oneReferensi(@Field("id") String id);

    @Multipart
    @POST("api/referensi/updateReferensi.php")
    Call<MessageModel> updateReferensi(@Part("id") RequestBody id,
                                       @Part("kode") RequestBody kode,
                                       @Part("nama") RequestBody nama,
                                       @Part("uraian") RequestBody uraian);

    class Factory{
        public static APIService create(){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.connectTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);

            OkHttpClient client = builder.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            return retrofit.create(APIService.class);
        }
    }
}
