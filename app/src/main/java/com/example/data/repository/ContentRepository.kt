package com.example.data.repository

import com.example.data.local.SavedItemDao
import com.example.data.local.SavedItemEntity
import com.example.data.model.ContentCategory
import com.example.data.model.ContentItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ContentRepository(private val savedItemDao: SavedItemDao) {

    fun getSavedItems(): Flow<List<ContentItem>> {
        return savedItemDao.getAllSavedItems().map { list ->
            list.map { it.toContentItem() }
        }
    }

    suspend fun saveItem(item: ContentItem) {
        savedItemDao.insertSavedItem(SavedItemEntity.fromContentItem(item))
    }

    suspend fun removeItem(id: String) {
        savedItemDao.deleteSavedItemById(id)
    }

    fun isSavedFlow(id: String): Flow<Boolean> {
        return savedItemDao.isItemSavedFlow(id)
    }

    suspend fun isSaved(id: String): Boolean {
        return savedItemDao.isItemSaved(id)
    }

    fun getAllContents(): List<ContentItem> {
        return staticContentList
    }

    fun getBreakingNews(): List<ContentItem> {
        return staticContentList.filter { it.isBreaking }
    }

    fun getContentsByCategory(category: ContentCategory): List<ContentItem> {
        if (category == ContentCategory.ALL) return staticContentList
        return staticContentList.filter { it.category == category }
    }

    fun getRaconQuotes(): List<ContentItem> {
        return staticContentList.filter { it.category == ContentCategory.RACON || it.quote != null }
    }

    companion object {
        val staticContentList = listOf(
            ContentItem(
                id = "savunma_kaan_5nesil",
                title = "Milli Gururumuz KAAN Gök Vatan'da: 5. Nesil Hayalet Avcı!",
                category = ContentCategory.SAVUNMA,
                summary = "TUSAŞ mühendislerinin yerli imkanlarla ürettiği KAAN, çift motoru, radara görünmezlik kabiliyeti ve üstün aviyonikleri ile dünyayı titretti.",
                fullBody = """Milli Muharip Uçak KAAN, Türk havacılık tarihinin en büyük gurur abidesi olarak göklerde süzülmeye devam ediyor. Dünyada sadece 4 ülkenin üretebildiği 5. nesil savaş uçağı teknolojisine sahip olan Türkiye, KAAN ile hava sahasında tam bağımsızlığını ilan etti.

TUSAŞ tesislerinde Türk mühendis ve teknisyenlerinin gece gündüz çalışmasıyla hayata geçirilen KAAN; süpersonik hız, dahili silah yuvaları, yüksek durumsal farkındalık sağlayan AESA radar ve elektronik harp sistemleri ile donatıldı.

Cumhurbaşkanımız Recep Tayyip Erdoğan'ın talimatıyla başlatılan Milli Teknoloji Hamlesi, Türkiye'yi savunmada dışa bağımlılıktan kurtarmış, yerlilik oranını %20'lerden %80'lerin üzerine çıkarmıştır. KAAN'ın seri üretime geçişiyle birlikte Türk Hava Kuvvetleri, bölgenin en caydırıcı gücü haline gelecektir.""",
                quote = "KAAN gök vatanla buluştu! Ne dediler? 'Yapamazlar, motor bulamazlar, kanadını takamazlar.' Elhamdülillah yaptık, uçurduk!",
                date = "23 Eylül 2026",
                source = "Savunma Sanayii Başkanlığı",
                readTimeMinutes = 4,
                imageResName = "banner_savunma_sanayii",
                raconAudioTitle = "KAAN Göklerde - Milli İrade",
                tags = listOf("KAAN", "TUSAŞ", "Milli Savunma", "Gök Vatan"),
                isBreaking = true
            ),
            ContentItem(
                id = "racon_dunya_5ten_buyuktur",
                title = "Reis'ten Tarihi Manifestosu: 'Dünya 5'ten Büyüktür!'",
                category = ContentCategory.RACON,
                summary = "Birleşmiş Milletler Genel Kurulu kürsüsünde tüm küresel emperyalist düzene meydan okuyan Erdoğan, mazlumların ve masumların gür sesi oldu.",
                fullBody = """Cumhurbaşkanı Recep Tayyip Erdoğan'ın uluslararası arenada ezberleri bozan ve tüm dünyada yankı uyandıran 'Dünya 5'ten Büyüktür' ilkesi, küresel adalet çağrısının bayrağı haline gelmiştir.

New York'taki BM Genel Kurulu'nda dünyanın gözünün içine bakarak bu hakikati haykıran Erdoğan: '5 daimi üyenin iki dudağı arasına sıkıştırılmış bir dünya adil olamaz! Mazlum milletlerin feryadını duymazdan gelenler, tarihin vicdanında mahkum olacaklardır.' demiştir.

Bu duruş; Türk milletinin sadece kendi sınırlarını değil, Kudüs'ten Saraybosna'ya, Karabağ'dan Afrika'ya kadar tüm mazlum coğrafyaların hakkını savunan kutlu bir medeniyet davasının ifadesidir.""",
                quote = "Dünya 5'ten büyüktür! Daha adil bir dünya mümkündür, ama asla küresel efendilerin himmetiyle değil, dik durarak!",
                date = "22 Eylül 2026",
                source = "Cumhurbaşkanlığı İletişim Başkanlığı",
                readTimeMinutes = 3,
                raconAudioTitle = "Dünya 5'ten Büyüktür - BM Çıkışı",
                tags = listOf("Reis", "Racon", "BM", "Dünya 5'ten Büyüktür"),
                isBreaking = true
            ),
            ContentItem(
                id = "racon_one_minute",
                title = "Davos'ta Masayı Titreten 'One Minute!' Çıkışı",
                category = ContentCategory.RACON,
                summary = "2009 Davos Zirvesi'nde İsrail Cumhurbaşkanı Şimon Peres'e ve moderatöre karşı verilen efsanevi racon, tarihe altın harflerle kazındı.",
                fullBody = """Davos'ta Gazze oturumunda yaşananlar, Türk diplomasi tarihinin ve liderlik duruşunun en unutulmaz anıdır. Masadaki adaletsiz süre dağılımına ve saldırgan üsluba karşı dik duran Erdoğan, tarihe geçen o sözleri söyledi:

'One minute! One minute! Sayın Peres, benden yaşlısın. Sesin çok yüksek çıkıyor. Biliyorum ki sesinin bu kadar çok yüksek çıkması bir suçluluk psikolojisinin gereğidir. Öldürmeye gelince, siz öldürmeyi çok iyi bilirsiniz! Plajlardaki çocukları nasıl vurduğunuzu çok iyi biliyorum...'

Ardından moderatörün engellemesine izin vermeyerek 'Benim için de bundan böyle Davos bitmiştir!' diyerek salonu terk eden Erdoğan, Türk milletinin başını bir kez daha dik tutmuştur.""",
                quote = "One minute! Siz öldürmeyi çok iyi bilirsiniz! Benim için de bundan böyle Davos bitmiştir!",
                date = "21 Eylül 2026",
                source = "Milli Arşiv",
                readTimeMinutes = 4,
                raconAudioTitle = "One Minute Tarihi Anı",
                tags = listOf("One Minute", "Davos", "Liderlik", "Dik Duruş")
            ),
            ContentItem(
                id = "savunma_kizilelma_tb3",
                title = "Kızılelma ve Bayraktar TB3: TCG Anadolu'da Tarih Yazıyor",
                category = ContentCategory.SAVUNMA,
                summary = "Dünyanın ilk SİHA gemisi TCG Anadolu'dan kısa pistli kalkış ve iniş yapan Kızılelma ve TB3, deniz havacılığında çığır açtı.",
                fullBody = """Baykar tarafından tamamen milli ve özgün imkanlarla geliştirilen Bayraktar Kızılelma insansız savaş uçağı ve katlanır kanatlı Bayraktar TB3 SİHA, Mavi Vatan'ın sancak gemisi TCG Anadolu üzerinde dünyada bir ilke imza attı.

Savunma sanayiinde yerli motor testlerini başarıyla tamamlayan Kızılelma, ses hızına yakın sürati, radar gizliliği ve hava-hava füzesi atış kabiliyeti ile geleceğin muharebe doktrinini yeniden şekillendiriyor.

Selçuk Bayraktar ve ekibinin 'Göklerde hür ve bağımsız Türkiye' hedefiyle yola çıktığı bu kutlu yolculuk, tüm dünyada askeri akademilerin ders kitaplarına girdi.""",
                quote = "Kimseden izin almadık, kimseye el açmadık. Alın teriyle, akıl teriyle Türk'ün mührünü göklere vurduk!",
                date = "20 Eylül 2026",
                source = "Baykar & Savunma Sanayii",
                readTimeMinutes = 3,
                imageResName = "banner_savunma_sanayii",
                raconAudioTitle = "Kızılelma - İHA Destanı",
                tags = listOf("Kızılelma", "TCG Anadolu", "Bayraktar", "Selçuk Bayraktar")
            ),
            ContentItem(
                id = "politika_turkiye_yuzyili_vizyon",
                title = "Cumhurbaşkanı Erdoğan: 'Türkiye Yüzyılı Başladı, Durmak Yok!'",
                category = ContentCategory.POLITIKA,
                summary = "Milletin hizmetinde geçen çeyrek asır: Eğitimden sağlığa, ulaşımdan savunmaya Türkiye Yüzyılı şahlanış dönemi ile taçlanıyor.",
                fullBody = """Cumhurbaşkanı Recep Tayyip Erdoğan, yaptığı konuşmada Türkiye Yüzyılı vizyonunun hedeflerini bir kez daha vurguladı. 

'Biz bu aziz millete efendi olmaya değil, hizmetkar olmaya geldik. 81 ilimizi köprülerle, barajlarla, şehir hastaneleriyle, bölünmüş yollarla donattık. Şimdi ise Türkiye Yüzyılı ile küresel güç olma yolundayız. Vesayet odaklarına, terör koridorlarına, ekonomik kumpaslara rağmen dimdik durduk, durmaya devam edeceğiz.'

Milletin sevgisi ve duasıyla yol yürüyen Erdoğan, Türkiye'nin kendi kaderini kendi çizen, masada ve sahada güçlü bir aktör olduğunu belirtti.""",
                quote = "Biz bu millete efendi olmaya değil, hizmetkâr olmaya geldik! Dik dur eğilme, bu millet seninle!",
                date = "19 Eylül 2026",
                source = "AK Parti Genel Merkezi",
                readTimeMinutes = 3,
                raconAudioTitle = "Hizmetkâr Olmaya Geldik",
                tags = listOf("Türkiye Yüzyılı", "Erdoğan", "Milli Vizyon", "İcraatlar")
            ),
            ContentItem(
                id = "teknoloji_togg_yollarda",
                title = "Milli Otomobilimiz TOGG: 100 Bininci Araç Yola Çıktı!",
                category = ContentCategory.TEKNOLOJI,
                summary = "Gemlik fabrikasında Türk mühendislerin ürettiği TOGG, akıllı cihaz ekosistemi ve yüksek batarya menzili ile Avrupa yollarında boy gösteriyor.",
                fullBody = """60 yıllık yerli otomobil rüyası gerçeğe dönüştü. Devrim otomobilinin yarım kalan hikayesini tamamlayan Cumhurbaşkanı Erdoğan ve Türkiye'nin Otomobili Girişim Grubu (TOGG), 100 bininci akıllı cihazı banttan indirdi.

T10X SUV modelinin ardından yeni sedan model T10F'in yollara çıkmasıyla birlikte TOGG ailesi genişliyor. Euro NCAP güvenlik testlerinden tam puan alan TOGG, milli batarya fabrikası Siro ile enerji bağımsızlığımıza da güç katıyor.

Vatandaşların yollarda gururla selamladığı TOGG, Türkiye'nin yüksek teknoloji üreten bir sanayi devine dönüştüğünün en somut kanıtı.""",
                quote = "Devrim arabasının önünü kesenlere cevabımızı TOGG ile verdik. İşte Türk'ün gücü, işte Türkiye!",
                date = "18 Eylül 2026",
                source = "Sanayi ve Teknoloji Bakanlığı",
                readTimeMinutes = 3,
                raconAudioTitle = "TOGG ve Milli Sanayi",
                tags = listOf("TOGG", "Gemlik", "Milli Teknoloji", "Yerli Otomobil")
            ),
            ContentItem(
                id = "ekonomi_gabar_karadeniz_gazi",
                title = "Gabar ve Karadeniz'den Müjde: Enerjide Bağımsızlık Meşalesi!",
                category = ContentCategory.EKONOMI,
                summary = "Gabar Dağı'nda günlük petrol üretimi 100 bin varili aştı. Filyos'ta işlenen Karadeniz Sakarya gazı evlerimize bereket taşıyor.",
                fullBody = """Terörden temizlenen Gabar ve Kato dağlarında petrol kuyuları ardı ardına açılıyor. Şehit Esma Çevik ve Şehit Aybüke Yalçın sahalarında üretilen kaliteli petrol, Türkiye'nin cari açığını kapatmada tarihi bir rol oynuyor.

Öte yandan Karadeniz Sakarya Gaz Sahası'nda Fatih, Yavuz, Kanuni ve Abdülhamid Han sondaj gemilerinin keşfettiği 710 milyar metreküplük milli doğalgaz, Filyos Doğal Gaz İşleme Tesisi'nden doğrudan ulusal şebekeye veriliyor.

Cumhurbaşkanı Erdoğan: 'Yıllarca bu dağlarda terör estirenlerin amacı milletimizin yer altı zenginliğini çalmaktı. Artık dağlarımızdan petrol, denizlerimizden gaz fışkırıyor!' müjdesini paylaştı.""",
                quote = "Dağlarımızdan petrol, denizlerimizden bereket fışkırıyor. Türkiye'nin enerjisini kimse kesemez!",
                date = "17 Eylül 2026",
                source = "Enerji ve Tabii Kaynaklar Bakanlığı",
                readTimeMinutes = 4,
                raconAudioTitle = "Gabar Petrolü ve Milli Gaz",
                tags = listOf("Gabar", "Karadeniz Gazı", "Enerji Bağımsızlığı", "Müjde")
            ),
            ContentItem(
                id = "racon_topunuz_gelin",
                title = "Reis'ten Vesayet Odaklarına: 'Topunuz Gelin, Topunuz!'",
                category = ContentCategory.RACON,
                summary = "İç ve dış vesayet odaklarının millet iradesini hedef alan kumpaslarına karşı tarihi meydan okuma.",
                fullBody = """Millet meydanlarında toplanan yüz binlere seslenen Erdoğan, milli iradeyi vesayet altına almak isteyen güç odaklarına karşı Türk siyasi tarihinin en sert ve kararlı duruşunu sergiledi.

'Bize kefen biçmeye kalkanlar bilsinler ki, biz bu yola beyaz kefenimizi giyerek çıktık! Ne içerideki taşeronlar ne de dışarıdaki efendileri bizi bu milletin yolundan çeviremez. Topunuz gelin, topunuz! Bu millete diz çöktüremeyeceksiniz, bu bayrağı indiremeyeceksiniz, bu ezanları dindiremeyeceksiniz!'

Bu tarihi sözler, milletin gönlünde bağımsızlık ve cesaret meşalesi olarak her daim yanmaktadır.""",
                quote = "Topunuz gelin, topunuz! Bu millete diz çöktüremeyeceksiniz, bu ezanları dindiremeyeceksiniz!",
                date = "16 Eylül 2026",
                source = "Millet Kürsüsü",
                readTimeMinutes = 2,
                raconAudioTitle = "Topunuz Gelin - Efsane Racon",
                tags = listOf("Racon", "Topunuz Gelin", "Milli İrade", "Cesaret")
            ),
            ContentItem(
                id = "tarih_fatih_sultan_mehmet",
                title = "Fatih Sultan Mehmet Han: Bir Çağı Kapatıp Yeni Çağı Açan Fethin Şifresi",
                category = ContentCategory.TARIH,
                summary = "21 yaşında gemileri karadan yürüten, şahi toplarını döktüren Fatih, kutlu fethin mimarı ve ecdadımızın gururudur.",
                fullBody = """Peygamber Efendimiz'in (S.A.V.) 'Kostantiniyye elbette fetholunacaktır. Onu fetheden kumandan ne güzel kumandan, onu fetheden asker ne güzel askerdir' müjdesine mazhar olan Fatih Sultan Mehmet Han, 1453'te İstanbul'u fethederek tarihin akışını değiştirdi.

Askeri dehası, fen ve ilimdeki derinliği, çok dilli bilgeliği ve gemileri Haliç'e karadan yürüten sarsılmaz inancıyla Bizans'ın aşılmaz surlarını dize getirdi. Fethin ardından Ayasofya'yı fethin sembolü ve cami olarak vakfetti.

Bugün Ayasofya-i Kebir Cami-i Şerifi'nin yeniden ibadete açılması, Fatih'in vasiyetine sadakatin ve ecdada vefanın en büyük göstergesidir.""",
                quote = "İmkanın sınırını görmek için imkansızı denemek lazım! Ya ben İstanbul'u alırım, ya İstanbul beni!",
                date = "15 Eylül 2026",
                source = "Tarih ve Medeniyet Enstitüsü",
                readTimeMinutes = 4,
                raconAudioTitle = "Fethin Ayak Sesleri",
                tags = listOf("Fatih", "İstanbul", "Fetih", "Ayasofya")
            ),
            ContentItem(
                id = "tarih_abdulhamid_han",
                title = "Sultan II. Abdülhamid Han: 33 Yıl Ayakta Kalan Deha Siyaseti",
                category = ContentCategory.TARIH,
                summary = "Kurtlar sofrasında devleti dağılmaktan koruyan, Hicaz Demiryolu'nu inşa eden Ulu Hakan'ın şanlı mücadelesi.",
                fullBody = """Osmanlı Devleti'nin en çalkantılı döneminde tahta geçen Sultan II. Abdülhamid Han, dahiyane dış siyaseti, istihbarat ağı ve modern eğitim-sağlık kurumlarıyla devleti 33 yıl boyunca ayakta tutmuştur.

Kendi şahsi servetini bağışlayarak İslam aleminin dayanışmasıyla inşa ettirdiği Hicaz Demiryolu, Medine-i Münevvere'ye ulaştığında Peygamberimizin ruhaniyeti rahatsız olmasın diye raylara keçe sardıracak kadar yüksek bir edep ve takva sahibiydi.

Filistin topraklarını dünya zenginliklerine rağmen siyonistlere vermeyen ve 'Ben bir karış dahi toprak satmam, zira bu vatan bana değil milletime aittir' diyen Ulu Hakan, ecdadımızın onur abidesidir.""",
                quote = "Ben bir karış dahi olsa toprak satmam, zira bu vatan bana değil, milletime aittir!",
                date = "14 Eylül 2026",
                source = "Ulu Hakan Arşivi",
                readTimeMinutes = 4,
                raconAudioTitle = "Ulu Hakan Abdülhamid Han",
                tags = listOf("Abdülhamid Han", "Hicaz Demiryolu", "Osmanlı", "Filistin")
            ),
            ContentItem(
                id = "savunma_celik_kubbe",
                title = "Çelik Kubbe: Türkiye'nin Gökyüzü Kalkanı Tam Entegre!",
                category = ContentCategory.SAVUNMA,
                summary = "ASELSAN, ROKETSAN ve TÜBİTAK SAGE ortaklığıyla katmanlı hava savunma ağı 'Çelik Kubbe' göklerimizi zırh gibi sarıyor.",
                fullBody = """Türkiye, hava sahasını her türlü tehdide karşı koruyacak çok katmanlı 'Çelik Kubbe' projesini hayata geçirdi. Alçak, orta ve yüksek irtifa hava savunma sistemleri, yerli yapay zeka ve radar ağları ile tek merkezden yönetiliyor.

Korkut namlulu alçak irtifa sistemi, Hisar-A+, Hisar-O+ ve uzun menzilli Siper hava savunma füzeleri, Çelik Kubbe çatısı altında birleşerek dışarıdan füze veya hava aracı sızmasını imkansız kılıyor.

ASELSAN'ın geliştirdiği AESA radarlar ve dost-düşman tanıma sistemleri sayesinde Türkiye, dünyada kendi entegre hava savunma kalkanına sahip sayılı ülkeler arasına girdi.""",
                quote = "Göklerimizde gözü olan bilsin ki, Çelik Kubbe'miz aşılmaz, Türk'ün kılıcı kınından çıkınca geri dönmez!",
                date = "13 Eylül 2026",
                source = "Savunma Sanayii İcra Komitesi",
                readTimeMinutes = 3,
                raconAudioTitle = "Çelik Kubbe - Gökyüzü Zırhı",
                tags = listOf("Çelik Kubbe", "Siper", "Hisar", "ASELSAN")
            ),
            ContentItem(
                id = "teknoloji_turksat_6a",
                title = "TÜRKSAT 6A: Uzayda Bayrağımız Dalgalanıyor!",
                category = ContentCategory.TEKNOLOJI,
                summary = "Türkiye'nin ilk yerli ve milli haberleşme uydusu TÜRKSAT 6A, 42 derece doğu yörüngesinde hizmete başladı.",
                fullBody = """Milli Teknoloji Hamlesi'nin uzaydaki gözbebeği TÜRKSAT 6A uydusu, Türk mühendislerinin tasarımı, yazılımı ve üretimi ile uzaydaki yerini aldı.

Uydunun fırlatılması ve yörüngeye yerleşmesiyle birlikte Türkiye, kendi haberleşme uydusunu üretebilen 11 ülkeden biri oldu. TÜRKSAT 6A; Türkiye'nin yanı sıra Hindistan, Tayland, Malezya ve Endonezya'yı da kapsayan devasa bir coğrafyada 5 milyardan fazla insana yayıncılık ve güvenli veri iletişimi sağlıyor.

Cumhurbaşkanı Erdoğan: 'Uzayda izi olmayanın dünyada sözü olmaz dedik, uzaya Türk mührünü vurduk!' dedi.""",
                quote = "Uzayda izi olmayanın dünyada sözü olmaz! TÜRKSAT 6A ile artık uzayda da söz sahibiyiz!",
                date = "12 Eylül 2026",
                source = "TÜRKSAT & Ulaştırma Bakanlığı",
                readTimeMinutes = 3,
                raconAudioTitle = "Uzay Vatan TÜRKSAT",
                tags = listOf("TÜRKSAT 6A", "Uzay Vatan", "Milli Uydu", "Haberleşme")
            )
        )
    }
}
