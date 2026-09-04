import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:url_launcher/url_launcher.dart';
import '../models/property.dart';
import '../theme/app_theme.dart';

class PropertyDetailScreen extends StatelessWidget {
  final Property property;
  final VoidCallback onToggleFavorite;
  final VoidCallback onOpenCalculator;

  const PropertyDetailScreen({
    super.key,
    required this.property,
    required this.onToggleFavorite,
    required this.onOpenCalculator,
  });

  Future<void> _makeCall(String phone) async {
    final uri = Uri.parse("tel:$phone");
    if (await canLaunchUrl(uri)) {
      await launchUrl(uri);
    }
  }

  Future<void> _openWhatsApp(String phone, String title) async {
    final cleanPhone = phone.replaceAll('+', '').replaceAll(' ', '');
    final message = Uri.encodeComponent("السلام عليكم، أستفسر بخصوص الإعلان: $title");
    final uri = Uri.parse("https://wa.me/$cleanPhone?text=$message");
    if (await canLaunchUrl(uri)) {
      await launchUrl(uri, mode: LaunchMode.externalApplication);
    }
  }

  @override
  Widget build(BuildContext context) {
    final currencyFormatter = NumberFormat('#,###', 'ar');
    final isRent = property.purpose == "للإيجار";

    return Scaffold(
      body: CustomScrollView(
        slivers: [
          SliverAppBar(
            expandedHeight: 280,
            pinned: true,
            flexibleSpace: FlexibleSpaceBar(
              background: Stack(
                fit: StackFit.expand,
                children: [
                  Image.network(
                    property.imageUrl,
                    fit: BoxFit.cover,
                    errorBuilder: (ctx, _, __) => Container(
                      color: AppTheme.darkSurfaceVariant,
                      child: const Icon(Icons.apartment, size: 70, color: AppTheme.goldAccent),
                    ),
                  ),
                  DecoratedBox(
                    decoration: BoxDecoration(
                      gradient: LinearGradient(
                        begin: Alignment.topCenter,
                        end: Alignment.bottomCenter,
                        colors: [
                          Colors.black.withOpacity(0.6),
                          Colors.transparent,
                          Colors.black.withOpacity(0.8),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
            actions: [
              IconButton(
                icon: Icon(
                  property.isFavorite ? Icons.favorite : Icons.favorite_border,
                  color: property.isFavorite ? AppTheme.favoriteRed : AppTheme.goldAccent,
                ),
                onPressed: onToggleFavorite,
              ),
            ],
          ),
          SliverToBoxAdapter(
            child: Padding(
              padding: const EdgeInsets.all(20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // شارات التصنيف
                  Row(
                    children: [
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                        decoration: BoxDecoration(
                          color: isRent ? AppTheme.tagGreenBg : AppTheme.goldAccent,
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Text(
                          property.purpose,
                          style: TextStyle(
                            color: isRent ? AppTheme.tagGreen : AppTheme.primaryOnColor,
                            fontWeight: FontWeight.bold,
                            fontSize: 12,
                          ),
                        ),
                      ),
                      const SizedBox(width: 8),
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                        decoration: BoxDecoration(
                          color: AppTheme.darkSurfaceVariant,
                          borderRadius: BorderRadius.circular(8),
                          border: Border.all(color: AppTheme.darkOutline),
                        ),
                        child: Text(
                          property.type,
                          style: const TextStyle(
                            color: AppTheme.textMain,
                            fontWeight: FontWeight.bold,
                            fontSize: 12,
                          ),
                        ),
                      ),
                      const Spacer(),
                      Text(
                        "${currencyFormatter.format(property.price)} ${property.currency}",
                        style: const TextStyle(
                          color: AppTheme.goldAccent,
                          fontSize: 22,
                          fontWeight: FontWeight.w900,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 16),
                  Text(
                    property.title,
                    style: const TextStyle(
                      fontSize: 20,
                      fontWeight: FontWeight.bold,
                      color: AppTheme.textMain,
                    ),
                  ),
                  const SizedBox(height: 8),
                  Row(
                    children: [
                      const Icon(Icons.location_on, size: 18, color: AppTheme.goldAccent),
                      const SizedBox(width: 4),
                      Text(
                        "${property.neighborhood}، ${property.city}، المملكة العربية السعودية",
                        style: const TextStyle(color: AppTheme.textSecondary, fontSize: 14),
                      ),
                    ],
                  ),
                  const SizedBox(height: 24),

                  // المواصفات الأساسية
                  const Text(
                    "مواصفات العقار",
                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 12),
                  Row(
                    children: [
                      Expanded(child: _specCard(Icons.square_foot, "${property.area.toInt()} م²", "المساحة")),
                      const SizedBox(width: 10),
                      Expanded(child: _specCard(Icons.bed, "${property.bedrooms}", "غرف النوم")),
                      const SizedBox(width: 10),
                      Expanded(child: _specCard(Icons.bathtub, "${property.bathrooms}", "دورات المياه")),
                    ],
                  ),
                  const SizedBox(height: 24),

                  // حاسبة التمويل العقاري السريع
                  Card(
                    color: AppTheme.darkSurfaceVariant,
                    child: Padding(
                      padding: const EdgeInsets.all(16),
                      child: Row(
                        children: [
                          const CircleAvatar(
                            backgroundColor: AppTheme.goldAccent,
                            child: Icon(Icons.calculate, color: AppTheme.primaryOnColor),
                          ),
                          const SizedBox(width: 14),
                          const Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  "حاسبة التمويل العقاري",
                                  style: TextStyle(fontWeight: FontWeight.bold, fontSize: 14),
                                ),
                                SizedBox(height: 2),
                                Text(
                                  "احسب القسط التقديري والدفعة الأولى",
                                  style: TextStyle(fontSize: 12, color: AppTheme.textSecondary),
                                ),
                              ],
                            ),
                          ),
                          ElevatedButton(
                            style: ElevatedButton.styleFrom(
                              backgroundColor: AppTheme.goldAccent,
                              foregroundColor: AppTheme.primaryOnColor,
                              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
                            ),
                            onPressed: () {
                              Navigator.pop(context);
                              onOpenCalculator();
                            },
                            child: const Text("احسب الآن", style: TextStyle(fontWeight: FontWeight.bold)),
                          ),
                        ],
                      ),
                    ),
                  ),
                  const SizedBox(height: 24),

                  // المرافق والمميزات
                  const Text(
                    "المميزات والمرافق",
                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 12),
                  Wrap(
                    spacing: 8,
                    runSpacing: 8,
                    children: property.amenities.map((amenity) {
                      return Container(
                        padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 8),
                        decoration: BoxDecoration(
                          color: AppTheme.darkSurfaceVariant,
                          borderRadius: BorderRadius.circular(20),
                          border: Border.all(color: AppTheme.darkOutline),
                        ),
                        child: Row(
                          mainAxisSize: MainAxisSize.min,
                          children: [
                            const Icon(Icons.check_circle, size: 16, color: AppTheme.goldAccent),
                            const SizedBox(width: 6),
                            Text(amenity, style: const TextStyle(fontSize: 13, color: AppTheme.textMain)),
                          ],
                        ),
                      );
                    }).toList(),
                  ),
                  const SizedBox(height: 24),

                  // الوصف
                  const Text(
                    "تفاصيل العقار",
                    style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    property.description,
                    style: const TextStyle(color: AppTheme.textSecondary, height: 1.6, fontSize: 14),
                  ),
                  const SizedBox(height: 24),

                  // بطاقة الوسيط المعتمد
                  Card(
                    color: AppTheme.darkSurfaceVariant,
                    child: Padding(
                      padding: const EdgeInsets.all(16),
                      child: Column(
                        children: [
                          Row(
                            children: [
                              CircleAvatar(
                                radius: 24,
                                backgroundColor: AppTheme.goldAccent,
                                child: Text(
                                  property.agentName.isNotEmpty ? property.agentName.substring(0, 1) : "ع",
                                  style: const TextStyle(
                                    fontSize: 18,
                                    fontWeight: FontWeight.bold,
                                    color: AppTheme.primaryOnColor,
                                  ),
                                ),
                              ),
                              const SizedBox(width: 14),
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      property.agentName,
                                      style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 15),
                                    ),
                                    const SizedBox(height: 2),
                                    Text(
                                      property.agentLicense,
                                      style: const TextStyle(color: AppTheme.goldAccent, fontSize: 12),
                                    ),
                                  ],
                                ),
                              ),
                            ],
                          ),
                          const SizedBox(height: 16),
                          Row(
                            children: [
                              Expanded(
                                child: ElevatedButton.icon(
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor: AppTheme.goldAccent,
                                    foregroundColor: AppTheme.primaryOnColor,
                                    padding: const EdgeInsets.symmetric(vertical: 12),
                                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                                  ),
                                  onPressed: () => _makeCall(property.agentPhone),
                                  icon: const Icon(Icons.phone),
                                  label: const Text("اتصال هاتفي", style: TextStyle(fontWeight: FontWeight.bold)),
                                ),
                              ),
                              const SizedBox(width: 10),
                              Expanded(
                                child: OutlinedButton.icon(
                                  style: OutlinedButton.styleFrom(
                                    foregroundColor: AppTheme.tagGreen,
                                    side: const BorderSide(color: AppTheme.tagGreen),
                                    padding: const EdgeInsets.symmetric(vertical: 12),
                                    shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
                                  ),
                                  onPressed: () => _openWhatsApp(property.agentPhone, property.title),
                                  icon: const Icon(Icons.chat),
                                  label: const Text("واتساب", style: TextStyle(fontWeight: FontWeight.bold)),
                                ),
                              ),
                            ],
                          ),
                        ],
                      ),
                    ),
                  ),
                  const SizedBox(height: 40),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _specCard(IconData icon, String value, String title) {
    return Container(
      padding: const EdgeInsets.symmetric(vertical: 14),
      decoration: BoxDecoration(
        color: AppTheme.darkSurfaceVariant,
        borderRadius: BorderRadius.circular(14),
        border: Border.all(color: AppTheme.darkOutline),
      ),
      child: Column(
        children: [
          Icon(icon, color: AppTheme.goldAccent, size: 22),
          const SizedBox(height: 6),
          Text(value, style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 14)),
          const SizedBox(height: 2),
          Text(title, style: const TextStyle(color: AppTheme.textSecondary, fontSize: 12)),
        ],
      ),
    );
  }
}
