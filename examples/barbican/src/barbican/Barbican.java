package barbican;

import com.google.inject.Exposed;
import com.google.inject.Injector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Singleton;
import org.immutables.eventual.EventualModules;
import org.immutables.eventual.Eventually;
import org.immutables.value.Value;

@Value.Immutable
interface TombOfLoric {}

@Value.Immutable
interface OldTower {}

@Value.Immutable
interface BonesOfLoric {}

@Value.Immutable
interface WindmillKey {}

@Value.Immutable
interface Bonedust {
  @Value.Parameter
  BonesOfLoric bones();
}

@Value.Immutable
interface PotionOfMithrill {
  @Value.Parameter
  Bonedust component();
}

@Value.Immutable
interface CastleKey {}

@Singleton
public class Barbican {

  @Eventually.Provides
  public TombOfLoric tomb() {
    return ImmutableTombOfLoric.of();
  }

  @Eventually.Provides
  public OldTower tower() {
    return ImmutableOldTower.of();
  }

  @Eventually.Provides
  public WindmillKey millKey(OldTower oldTower) {
    tickProgress(4, "getting mill key from the " + oldTower);
    return ImmutableWindmillKey.of();
  }

  @Eventually.Provides
  public BonesOfLoric bones(TombOfLoric tombOfLoric) {
    tickProgress(5, "finding bones in " + tombOfLoric);
    return ImmutableBonesOfLoric.of();
  }

  @Eventually.Provides
  public Bonedust bonedust(WindmillKey key, BonesOfLoric bones) {
    tickProgress(5, "bonedust milled on the windmill opened using key " + key);
    return ImmutableBonedust.of(bones);
  }

  @Eventually.Provides
  public PotionOfMithrill potion(Bonedust bonedust) {
    tickProgress(3, "brew potiono of mithril transmutation");
    return ImmutablePotionOfMithrill.of(bonedust);
  }

  @Exposed // This is only end result we want so we expose only it
  @Eventually.Provides
  public CastleKey castleKey(PotionOfMithrill potion) {
    tickProgress(1, "turning mithril wall into wood using " + potion);
    tickProgress(1, "breaking wooden wall");
    tickProgress(1, "getting castle key");
    return ImmutableCastleKey.of();
  }

  public static void main(String... args) {

    ExecutorService executor = Executors.newCachedThreadPool();

    Injector injector = new EventualModules.Builder()
        .add(Barbican.class)
        .executor(executor)
        .joinInjector();

    CastleKey castleKey = injector.getInstance(CastleKey.class);

    System.out.println("At last! We can unlock the castle gates using the " + castleKey);

    executor.shutdown();
  }

  /**
   * We're ticking progress so that it is visible that some things take time and happen in parallel
   * where appropriate.
   */
  private static void tickProgress(int times, String millKey) {
    for (int i = 0; i < times; i++) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException ex) {
        throw new RuntimeException(ex);
      }
      int percentage = (int) (((i + 1) / (float) times) * 100);
      System.out.printf("%d%% %s\n", percentage, millKey);
    }
  }
}
